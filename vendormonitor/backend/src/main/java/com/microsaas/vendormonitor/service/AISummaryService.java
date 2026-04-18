package com.microsaas.vendormonitor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.vendormonitor.domain.Contract;
import com.microsaas.vendormonitor.domain.PerformanceRecord;
import com.microsaas.vendormonitor.domain.RenewalSummary;
import com.microsaas.vendormonitor.domain.Vendor;
import com.microsaas.vendormonitor.repository.ContractRepository;
import com.microsaas.vendormonitor.repository.PerformanceRecordRepository;
import com.microsaas.vendormonitor.repository.RenewalSummaryRepository;
import com.microsaas.vendormonitor.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AISummaryService {

    private final VendorRepository vendorRepository;
    private final ContractRepository contractRepository;
    private final PerformanceRecordRepository performanceRecordRepository;
    private final RenewalSummaryRepository renewalSummaryRepository;
    private final RestTemplate restTemplate;

    @Value("${cc.ai.gateway-url:http://localhost:11434/api/generate}")
    private String aiGatewayUrl;

    @Value("${cc.ai.api-key:dummy-key}")
    private String aiApiKey;

    @Autowired
    public AISummaryService(VendorRepository vendorRepository,
                            ContractRepository contractRepository,
                            PerformanceRecordRepository performanceRecordRepository,
                            RenewalSummaryRepository renewalSummaryRepository,
                            RestTemplate restTemplate) {
        this.vendorRepository = vendorRepository;
        this.contractRepository = contractRepository;
        this.performanceRecordRepository = performanceRecordRepository;
        this.renewalSummaryRepository = renewalSummaryRepository;
        this.restTemplate = restTemplate;
    }

    public RenewalSummary generateSummary(UUID vendorId) {
        UUID tenantId = TenantContext.require();
        Vendor vendor = vendorRepository.findByIdAndTenantId(vendorId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        List<Contract> contracts = contractRepository.findByTenantIdAndVendorId(tenantId, vendorId);
        List<PerformanceRecord> records = performanceRecordRepository.findByTenantIdAndVendorId(tenantId, vendorId);

        String prompt = buildPrompt(vendor, contracts, records);
        String aiResponse = callAiModel(prompt);

        RenewalSummary summary = new RenewalSummary();
        summary.setTenantId(tenantId);
        summary.setVendor(vendor);
        if (!contracts.isEmpty()) {
            summary.setContract(contracts.get(0));
        }
        summary.setGeneratedAt(ZonedDateTime.now());
        
        // Parse AI response (basic parsing for demo purposes)
        summary.setOverallScore(extractScore(aiResponse));
        summary.setRecommendation(extractRecommendation(aiResponse));
        summary.setAiSummary(aiResponse);

        return renewalSummaryRepository.save(summary);
    }
    
    public List<RenewalSummary> getSummariesForVendor(UUID vendorId) {
        return renewalSummaryRepository.findByTenantIdAndVendorId(TenantContext.require(), vendorId);
    }
    
    public Optional<RenewalSummary> getSummaryById(UUID id) {
        return renewalSummaryRepository.findByIdAndTenantId(id, TenantContext.require());
    }

    private String buildPrompt(Vendor vendor, List<Contract> contracts, List<PerformanceRecord> records) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the performance for vendor: ").append(vendor.getName()).append("\n");
        prompt.append("SLA Description: ").append(vendor.getSlaDescription()).append("\n\n");
        
        prompt.append("Contracts:\n");
        for (Contract c : contracts) {
            prompt.append("- Value: ").append(c.getValueAmount()).append(" ").append(c.getValueCurrency());
            prompt.append(", Target Uptime: ").append(c.getSlaUptimePercentage()).append("%");
            prompt.append(", Target Response Time: ").append(c.getSlaResponseTimeMinutes()).append(" mins\n");
        }
        
        prompt.append("\nRecent Performance Records:\n");
        long breachCount = 0;
        for (PerformanceRecord r : records) {
            prompt.append("- [").append(r.getRecordType()).append("] ").append(r.getMetricValue()).append(" ").append(r.getMetricUnit());
            if (Boolean.TRUE.equals(r.getIsSlaBreach())) {
                prompt.append(" (BREACH)");
                breachCount++;
            }
            prompt.append("\n");
        }
        
        prompt.append("\nBased on this data, provide:\n");
        prompt.append("1. An overall score from 0 to 100.\n");
        prompt.append("2. A recommendation which MUST be exactly one of: RENEW, RENEGOTIATE, or CANCEL.\n");
        prompt.append("3. A summary explanation.\n");
        
        return prompt.toString();
    }

    private String callAiModel(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + aiApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama3"); // Fallback or configured model
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(aiGatewayUrl, request, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (String) response.getBody().get("response");
            }
        } catch (Exception e) {
            // Log error
            System.err.println("Error calling AI: " + e.getMessage());
        }
        return "Score: 75\nRecommendation: RENEGOTIATE\nSummary: AI service unavailable, placeholder response.";
    }
    
    private int extractScore(String text) {
        if (text.contains("Score: ")) {
            try {
                String scoreStr = text.split("Score: ")[1].split("\n")[0].trim();
                return Integer.parseInt(scoreStr);
            } catch (Exception e) {
                return 75; // Default fallback
            }
        }
        return 75;
    }
    
    private String extractRecommendation(String text) {
        if (text.contains("RENEW")) return "RENEW";
        if (text.contains("CANCEL")) return "CANCEL";
        return "RENEGOTIATE";
    }
}
