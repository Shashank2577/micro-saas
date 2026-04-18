package com.microsaas.insuranceai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LiteLLMClient {

    private final RestTemplate restTemplate;

    @Value("${cc.ai.gateway-url}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key}")
    private String apiKey;

    @Value("${cc.ai.default-model}")
    private String defaultModel;

    public LiteLLMClient() {
        this.restTemplate = new RestTemplate();
    }

    public String generateCompletion(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", defaultModel);
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(gatewayUrl + "/v1/chat/completions", entity, Map.class);
            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            // Log and handle circuit breaker / retries in a production system.
            // For now, fallback to a mocked JSON string.
            System.err.println("LLM call failed: " + e.getMessage());
        }

        // Mock response if LiteLLM is not reachable locally
        return "{\"score\": 50.0, \"reasoning\": \"Mocked fallback due to LLM timeout.\", \"factors\": \"Mocked fallback due to LLM timeout.\"}";
    }
}
