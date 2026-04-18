package com.microsaas.careerpath.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

@Component
public class LiteLLMClient {

    private final RestTemplate restTemplate;

    @Value("${ai.litellm.url:http://litellm:4000/v1/chat/completions}")
    private String litellmUrl;

    @Value("${ai.litellm.api-key:sk-1234}")
    private String litellmApiKey;

    public LiteLLMClient() {
        this.restTemplate = new RestTemplate();
    }

    public String generateCompletion(String systemPrompt, String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(litellmApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4-turbo"); // Use Litellm proxy model name
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(litellmUrl, entity, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            // Log error
            System.err.println("Error calling LiteLLM: " + e.getMessage());
        }
        return "Failed to generate AI response. Please try again later.";
    }
}
