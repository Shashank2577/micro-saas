package com.microsaas.realestateitel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.realestateitel.domain.Lease;
import com.microsaas.realestateitel.domain.Property;
import com.microsaas.realestateitel.repository.LeaseRepository;
import com.microsaas.realestateitel.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaseService {

    private final LeaseRepository leaseRepository;
    private final PropertyRepository propertyRepository;
    private final RestTemplate restTemplate;

    @Value("${cc.ai.gateway-url:http://localhost:8080}")
    private String aiGatewayUrl;

    @Value("${cc.ai.api-key:secret}")
    private String aiApiKey;

    public Lease getLeaseById(UUID id) {
        return leaseRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Lease not found"));
    }

    public List<Lease> getLeasesForProperty(UUID propertyId) {
        return leaseRepository.findByTenantIdAndPropertyId(TenantContext.require(), propertyId);
    }

    @Transactional
    public Lease processLeaseText(String text, UUID propertyId) {
        UUID tenantId = TenantContext.require();
        Property property = propertyRepository.findByIdAndTenantId(propertyId, tenantId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        String aiSummary = abstractLeaseWithLiteLLM(text);

        Lease lease = new Lease();
        lease.setTenantId(tenantId);
        lease.setProperty(property);
        
        // Mocking extraction of structured data
        lease.setTenantName("Extracted Tenant Name");
        lease.setStartDate(LocalDate.now());
        lease.setEndDate(LocalDate.now().plusYears(1));
        lease.setMonthlyRent(BigDecimal.valueOf(2000));
        lease.setSecurityDeposit(BigDecimal.valueOf(2000));
        lease.setAiSummary(aiSummary);
        
        return leaseRepository.save(lease);
    }

    private String abstractLeaseWithLiteLLM(String leaseText) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(aiApiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4");
            List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are an AI that abstracts lease documents. Summarize the key terms."),
                Map.of("role", "user", "content", "Abstract this lease: " + leaseText)
            );
            body.put("messages", messages);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(aiGatewayUrl + "/v1/chat/completions", request, Map.class);
            
            Map<String, Object> respBody = response.getBody();
            if (respBody != null && respBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) respBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            return "Failed to extract summary.";
        } catch (Exception e) {
            return "AI Summary unavailable due to gateway error.";
        }
    }
}
