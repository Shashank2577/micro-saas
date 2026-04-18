package com.microsaas.restaurantintel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class LiteLLMClient {

    private final RestTemplate restTemplate;

    @Value("${cc.ai.gateway-url}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key}")
    private String apiKey;

    public LiteLLMClient() {
        this.restTemplate = new RestTemplate();
    }

    public String completeChat(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-4",
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are an AI assistant for a restaurant operations platform."),
                            Map.of("role", "user", "content", prompt)
                    )
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // Assuming standard OpenAI format response from LiteLLM
            Map response = restTemplate.postForObject(gatewayUrl + "/chat/completions", request, Map.class);
            if (response != null && response.containsKey("choices")) {
                List choices = (List) response.get("choices");
                if (!choices.isEmpty()) {
                    Map choice = (Map) choices.get(0);
                    Map message = (Map) choice.get("message");
                    return (String) message.get("content");
                }
            }
            return "Unable to parse AI response";
        } catch (Exception e) {
            return "AI Analysis failed: " + e.getMessage();
        }
    }
}
