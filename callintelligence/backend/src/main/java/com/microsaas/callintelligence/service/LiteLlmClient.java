package com.microsaas.callintelligence.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LiteLlmClient {

    private final RestTemplate restTemplate;
    private final String gatewayUrl;
    private final String apiKey;
    private final String defaultModel;

    public LiteLlmClient(RestTemplate restTemplate, 
                         @Value("${cc.ai.gateway-url}") String gatewayUrl,
                         @Value("${cc.ai.api-key}") String apiKey,
                         @Value("${cc.ai.default-model}") String defaultModel) {
        this.restTemplate = restTemplate;
        this.gatewayUrl = gatewayUrl;
        this.apiKey = apiKey;
        this.defaultModel = defaultModel;
    }

    public String analyzeWithRetry(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", defaultModel);
        requestBody.put("messages", List.of(
            Map.of("role", "system", "content", "You are an AI sales intelligence engine. Output ONLY valid JSON."),
            Map.of("role", "user", "content", prompt)
        ));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                ResponseEntity<Map> response = restTemplate.exchange(
                    gatewayUrl + "/v1/chat/completions",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
                );
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return (String) message.get("content");
                    }
                }
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    System.err.println("Failed to reach LiteLLM after " + maxRetries + " attempts: " + e.getMessage());
                }
            }
        }
        
        return "{}";
    }
}
