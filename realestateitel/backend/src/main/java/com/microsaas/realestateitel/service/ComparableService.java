package com.microsaas.realestateitel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.realestateitel.domain.Comparable;
import com.microsaas.realestateitel.domain.Property;
import com.microsaas.realestateitel.repository.ComparableRepository;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComparableService {

    private final ComparableRepository comparableRepository;
    private final PropertyRepository propertyRepository;
    private final RestTemplate restTemplate;

    @Value("${cc.ai.gateway-url:http://localhost:8080}")
    private String aiGatewayUrl;

    @Value("${cc.ai.api-key:secret}")
    private String aiApiKey;

    public List<Comparable> getComparablesForProperty(UUID subjectPropertyId) {
        return comparableRepository.findByTenantIdAndSubjectPropertyId(TenantContext.require(), subjectPropertyId);
    }

    @Transactional
    public List<Comparable> generateComparables(UUID subjectPropertyId) {
        UUID tenantId = TenantContext.require();
        Property subject = propertyRepository.findByIdAndTenantId(subjectPropertyId, tenantId)
                .orElseThrow(() -> new RuntimeException("Subject property not found"));

        List<Property> otherProperties = propertyRepository.findByTenantId(tenantId);
        otherProperties.removeIf(p -> p.getId().equals(subjectPropertyId));

        comparableRepository.deleteByTenantIdAndSubjectPropertyId(tenantId, subjectPropertyId);

        // In a real scenario, we would send the subject property and candidate properties to LiteLLM
        // For this mock, we just use LiteLLM to get a "similarity reasoning" and generate scores locally.
        
        for (Property compProp : otherProperties) {
            if (compProp.getPropertyType().equals(subject.getPropertyType())) {
                String aiReasoning = callLiteLLMForSimilarity(subject, compProp);
                
                Comparable comp = new Comparable();
                comp.setTenantId(tenantId);
                comp.setSubjectProperty(subject);
                comp.setCompProperty(compProp);
                
                // Mock calculation based on some factors
                BigDecimal score = BigDecimal.valueOf(0.85); // Dummy score
                comp.setSimilarityScore(score);
                comp.setPriceAdjusted(BigDecimal.valueOf(500000)); // Dummy price
                comp.setNotes(aiReasoning);
                
                comparableRepository.save(comp);
            }
        }
        
        return comparableRepository.findByTenantIdAndSubjectPropertyId(tenantId, subjectPropertyId);
    }

    private String callLiteLLMForSimilarity(Property p1, Property p2) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(aiApiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4");
            List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are an AI real estate assistant."),
                Map.of("role", "user", "content", "Compare these two properties and provide a brief reasoning for their similarity. P1: " + p1.getAddress() + ", P2: " + p2.getAddress())
            );
            body.put("messages", messages);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(aiGatewayUrl + "/v1/chat/completions", request, Map.class);
            
            // Extracting message (simplistic approach for demo)
            Map<String, Object> respBody = response.getBody();
            if (respBody != null && respBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) respBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            return "Analyzed by AI.";
        } catch (Exception e) {
            // Fallback if AI is unavailable
            return "AI Gateway unavailable. Similarity assumed based on property type.";
        }
    }
}
