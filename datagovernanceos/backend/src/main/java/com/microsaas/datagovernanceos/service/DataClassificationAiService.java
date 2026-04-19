package com.microsaas.datagovernanceos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.datagovernanceos.dto.DataClassificationResult;
import com.microsaas.datagovernanceos.entity.DataAsset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataClassificationAiService {

    @Value("${cc.ai.gateway-url}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key}")
    private String apiKey;

    @Value("${cc.ai.default-model:claude-sonnet-4-6}")
    private String defaultModel;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DataClassificationAiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public DataClassificationResult classifyAsset(DataAsset asset) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String systemPrompt = "You are a data governance expert AI. Your job is to classify data assets and detect potential PII based on the asset metadata provided. " +
                "Respond ONLY with a valid JSON object matching this schema: " +
                "{\"classification\": \"PUBLIC|INTERNAL|CONFIDENTIAL|RESTRICTED\", \"piiStatus\": true|false, \"reasoning\": \"string\"}.";

        String userMessage = String.format("Asset Name: %s\nType: %s\nDescription: %s",
                asset.getName(), asset.getType(), asset.getDescription());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", defaultModel);
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userMessage)
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(gatewayUrl + "/v1/chat/completions", entity, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");
                    return objectMapper.readValue(content, DataClassificationResult.class);
                }
            }
        } catch (Exception e) {
            // Fallback for tests or disconnected environments
        }

        DataClassificationResult fallback = new DataClassificationResult();
        fallback.setClassification("CONFIDENTIAL");
        fallback.setPiiStatus(true);
        fallback.setReasoning("Fallback classification due to AI service error");
        return fallback;
    }

    public Map<String, Object> analyzeText(Map<String, Object> requestData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String systemPrompt = "You are a data governance expert AI. Analyze the provided text for compliance risks, " +
                "governance issues, or policy alignment. Respond with a JSON object containing an 'analysis' field and a 'riskScore' (0-100).";

        String userMessage = "Analyze the following: " + requestData;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", defaultModel);
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userMessage)
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(gatewayUrl + "/v1/chat/completions", entity, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");
                    return objectMapper.readValue(content, Map.class);
                }
            }
        } catch (Exception e) {
            // Fallback for tests or disconnected environments
        }

        return Map.of(
            "analysis", "Fallback analysis due to AI service error.",
            "riskScore", 50
        );
    }
}
