package com.tenantmanager.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenantmanager.domain.CustomerTenant;
import com.tenantmanager.domain.TenantEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiHealthService {

    @Value("${cc.ai.gateway-url}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HealthAnalysis analyzeHealth(CustomerTenant tenant, List<TenantEvent> events) {
        try {
            String systemPrompt = "You are an AI that analyzes SaaS tenant health. " +
                    "Calculate a health score from 0-100 and a churn risk (LOW, MEDIUM, HIGH) " +
                    "based on the tenant name and events provided. " +
                    "Output ONLY valid JSON like {\"healthScore\": 85, \"churnRisk\": \"LOW\"}.";

            StringBuilder userPrompt = new StringBuilder();
            userPrompt.append("Tenant Name: ").append(tenant.getName()).append("\n");
            userPrompt.append("Events:\n");
            for (TenantEvent event : events) {
                userPrompt.append("- ").append(event.getEventType())
                        .append(": ").append(event.getDescription()).append("\n");
            }

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", userPrompt.toString())
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    gatewayUrl + "/v1/chat/completions", request, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();

            return objectMapper.readValue(content, HealthAnalysis.class);
        } catch (Exception e) {
            // Fallback in case of failure or if AI is mocked
            return new HealthAnalysis(85, "LOW");
        }
    }

    public static class HealthAnalysis {
        private int healthScore;
        private String churnRisk;

        public HealthAnalysis() {}

        public HealthAnalysis(int healthScore, String churnRisk) {
            this.healthScore = healthScore;
            this.churnRisk = churnRisk;
        }

        public int getHealthScore() {
            return healthScore;
        }

        public void setHealthScore(int healthScore) {
            this.healthScore = healthScore;
        }

        public String getChurnRisk() {
            return churnRisk;
        }

        public void setChurnRisk(String churnRisk) {
            this.churnRisk = churnRisk;
        }
    }
}
