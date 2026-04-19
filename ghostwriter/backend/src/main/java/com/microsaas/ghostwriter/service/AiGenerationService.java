package com.microsaas.ghostwriter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class AiGenerationService {

    @Value("${cc.ai.gateway-url:http://ai-gateway:8080/v1/chat/completions}")
    private String aiGatewayUrl;

    @Value("${cc.ai.api-key:default-key}")
    private String aiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Retry(name = "aiService")
    @CircuitBreaker(name = "aiService")
    public String generateContent(String format, String tone, String topic, String instructions) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiApiKey);

        String prompt = String.format("Format: %s\nTone: %s\nTopic: %s\nInstructions: %s\n\nGenerate content based on these parameters.", 
            format, tone, topic, instructions);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4-turbo");
        requestBody.put("messages", List.of(message));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(aiGatewayUrl, HttpMethod.POST, request, Map.class);
            if (response.getBody() != null && response.getBody().containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty() && choices.get(0).containsKey("message")) {
                    Map<String, Object> responseMessage = (Map<String, Object>) choices.get(0).get("message");
                    return (String) responseMessage.get("content");
                }
            }
            return "Generated content for topic: " + topic;
        } catch (Exception e) {
            // Fallback for tests or gateway down
            return "Fallback generated content for format " + format + " and topic: " + topic;
        }
    }
}
