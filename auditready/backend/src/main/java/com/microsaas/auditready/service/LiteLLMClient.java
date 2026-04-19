package com.microsaas.auditready.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Service
public class LiteLLMClient {

    private final RestTemplate restTemplate;
    
    @Value("${cc.ai.gateway-url}")
    private String gatewayUrl;
    
    @Value("${cc.ai.api-key}")
    private String apiKey;

    public LiteLLMClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "aiService")
    @CircuitBreaker(name = "aiService")
    public String askAi(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            
            Map<String, Object> body = Map.of(
                "model", "gpt-4",
                "messages", List.of(Map.of("role", "user", "content", prompt))
            );
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(gatewayUrl + "/v1/chat/completions", request, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            return "AI response unavailable.";
        } catch (Exception e) {
            return "Failed to contact AI service: " + e.getMessage();
        }
    }
}
