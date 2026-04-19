package com.micro.interviewos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AIService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${litellm.url:http://localhost:4000/v1/chat/completions}")
    private String litellmUrl;

    @Value("${litellm.key:dummy_key}")
    private String litellmKey;

    @CircuitBreaker(name = "aiService", fallbackMethod = "analyzeFallback")
    @Retry(name = "aiService")
    public Map<String, String> analyze(UUID tenantId, Map<String, Object> request) {
        String prompt = (String) request.getOrDefault("prompt", "Analyze the given text.");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(litellmKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", new Object[]{
            Map.of("role", "user", "content", prompt)
        });

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(litellmUrl, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Map.of("status", "analyzed", "result", "Success");
        }
        return Map.of("status", "analyzed", "result", "Failed");
    }

    public Map<String, String> analyzeFallback(UUID tenantId, Map<String, Object> request, Throwable t) {
        return Map.of("status", "analyzed", "result", "Fallback response due to: " + t.getMessage());
    }
}
