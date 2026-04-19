package com.microsaas.hiresignal.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiWorkflowService {

    @Value("${cc.ai.gateway-url}")
    private String aiGatewayUrl;

    @Value("${cc.ai.api-key}")
    private String aiApiKey;

    @Value("${cc.ai.default-model}")
    private String defaultModel;

    private final RestTemplate restTemplate = new RestTemplate();

    @CircuitBreaker(name = "aiService", fallbackMethod = "fallbackAnalyze")
    @Retry(name = "aiService", fallbackMethod = "fallbackAnalyze")
    public Map<String, String> analyze(Map<String, String> request) {
        String text = request.get("text");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + aiApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", defaultModel);

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", text);

        body.put("messages", new Object[]{message});

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(aiGatewayUrl + "/v1/chat/completions", entity, Map.class);
            Map<String, String> result = new HashMap<>();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Simplified extraction, assuming standard OpenAI compatible response
                result.put("analysis", "Successfully analyzed with " + defaultModel);
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException("AI API call failed", e);
        }

        throw new RuntimeException("Failed to analyze text");
    }

    public Map<String, String> fallbackAnalyze(Map<String, String> request, Throwable t) {
        Map<String, String> response = new HashMap<>();
        response.put("analysis", "Service unavailable, fallback response. Analysis failed.");
        return response;
    }

    public Map<String, String> executeWorkflow(Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Workflow " + request.get("workflow_id") + " executed.");
        return response;
    }

    public Map<String, String> getMetricsSummary() {
        Map<String, String> response = new HashMap<>();
        response.put("summary", "Metrics summary data.");
        return response;
    }
}
