package com.crosscutting.socialintelligence.client;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.List;

@Component
public class LiteLLMClient {

    private final RestTemplate restTemplate;
    private final String url;
    private final String model;
    private final String apiKey;

    public LiteLLMClient(RestTemplate restTemplate,
                         @Value("${litellm.url}") String url,
                         @Value("${litellm.model}") String model,
                         @Value("${litellm.api-key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.url = url;
        this.model = model;
        this.apiKey = apiKey;
    }

    public String generateCompletion(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url + "/v1/chat/completions", request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            // Log and handle fallback
            System.err.println("LiteLLM call failed: " + e.getMessage());
        }
        return "AI response unavailable";
    }
}
