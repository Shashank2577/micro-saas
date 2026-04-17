package com.microsaas.peopleanalytics.client;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Component
public class LiteLLMClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${cc.ai.url:http://localhost:4000/v1/chat/completions}")
    private String baseUrl;

    @Value("${cc.ai.model:gpt-3.5-turbo}")
    private String modelName;

    public String getInsights(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("model", modelName);

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);
            messages.add(userMsg);

            body.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            // Quick check for stub, mock response
            if (baseUrl.contains("stub") || "stub".equals(modelName)) {
                return "Mocked AI Insight.";
            }

            ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            return "No insights generated.";
        } catch (Exception e) {
            return "Mocked AI Insight due to timeout/error.";
        }
    }
}
