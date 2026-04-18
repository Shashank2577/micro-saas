package com.microsaas.prospectiq.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class LiteLLMClient {

    private final RestTemplate restTemplate;

    @Value("${cc.ai.gateway-url}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key}")
    private String apiKey;

    @Value("${cc.ai.default-model}")
    private String defaultModel;

    public String generateBrief(String prompt) {
        log.info("Calling LiteLLM for brief generation...");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", defaultModel);
        requestBody.put("messages", List.of(message));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            Map response = restTemplate.postForObject(gatewayUrl + "/v1/chat/completions", request, Map.class);
            if (response != null && response.containsKey("choices")) {
                List choices = (List) response.get("choices");
                if (!choices.isEmpty()) {
                    Map choice = (Map) choices.get(0);
                    Map messageResponse = (Map) choice.get("message");
                    return (String) messageResponse.get("content");
                }
            }
            return "Generated Brief (Fallback)";
        } catch (Exception e) {
            log.error("Failed to generate brief from LiteLLM: {}", e.getMessage());
            return "Failed to generate brief. Please try again later.";
        }
    }
}
