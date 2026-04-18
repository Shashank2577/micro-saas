package com.microsaas.cacheoptimizer.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@Service
public class LiteLlmPredictionService {

    @Value("${litellm.url:http://localhost:4000/v1/chat/completions}")
    private String litellmUrl;

    @Value("${litellm.api-key:dummy-key}")
    private String litellmApiKey;

    private final RestTemplate restTemplate;

    public LiteLlmPredictionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String predictWarmingKeys(String analyticsData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(litellmApiKey);

        String prompt = "Based on the following cache analytics, suggest the top 5 keys to warm up: " + analyticsData;

        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(litellmUrl, entity, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            // Log and handle fallback
            System.err.println("LiteLLM prediction failed: " + e.getMessage());
        }
        return "[]";
    }
}
