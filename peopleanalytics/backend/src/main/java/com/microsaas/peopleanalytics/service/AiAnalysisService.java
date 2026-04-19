package com.microsaas.peopleanalytics.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AiAnalysisService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${cc.ai.gateway-url:http://localhost:4000}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key:sk-local-dev-key}")
    private String apiKey;

    @Retry(name = "aiService", fallbackMethod = "fallbackAnalysis")
    @CircuitBreaker(name = "aiService", fallbackMethod = "fallbackAnalysis")
    public Map<String, String> analyze(Map<String, Object> request, UUID tenantId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("X-Tenant-ID", tenantId.toString());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        // Call LiteLLM endpoint
        try {
            return restTemplate.postForObject(gatewayUrl + "/v1/chat/completions", entity, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("AI Analysis failed", e);
        }
    }

    public Map<String, String> fallbackAnalysis(Map<String, Object> request, UUID tenantId, Throwable t) {
        Map<String, String> fallbackResponse = new HashMap<>();
        fallbackResponse.put("result", "AI analysis is currently unavailable. Please try again later. Error: " + t.getMessage());
        return fallbackResponse;
    }
}
