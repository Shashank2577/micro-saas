package com.microsaas.notificationhub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentOptimizationService {

    @Value("${cc.ai.litellm.base-url:http://localhost:4000}")
    private String litellmBaseUrl;

    @Value("${cc.ai.litellm.api-key:sk-1234}")
    private String litellmApiKey;

    @Value("${cc.ai.litellm.model:gpt-4}")
    private String litellmModel;

    private final RestTemplate restTemplate = new RestTemplate();

    public String optimizeContent(String originalContent, String goal) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(litellmApiKey);

            String prompt = String.format("Optimize the following notification content for %s. Only return the optimized content, no other text.\n\nContent: %s", goal, originalContent);

            Map<String, Object> requestBody = Map.of(
                    "model", litellmModel,
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    ),
                    "temperature", 0.7
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(litellmBaseUrl + "/v1/chat/completions", entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            log.warn("Failed to optimize content using LiteLLM, falling back to original content", e);
        }
        return originalContent;
    }
}
