package com.microsaas.meetingbrain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.meetingbrain.dto.AnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);

    @Value("${cc.ai.gateway-url}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key}")
    private String apiKey;

    @Value("${cc.ai.default-model:claude-3-sonnet-20240229}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AiService(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public AnalysisResult analyzeTranscript(String transcript) {
        log.info("Analyzing transcript of length {}", transcript.length());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String systemPrompt = "You are an AI meeting assistant. Analyze the following meeting transcript. " +
                "Extract a summary, decisions made, action items, and open questions. " +
                "Respond ONLY with a valid JSON object following this schema: " +
                "{\"summary\": \"string\", \"decisions\": [{\"topic\": \"string\", \"description\": \"string\", \"decisionText\": \"string\"}], " +
                "\"actionItems\": [{\"description\": \"string\", \"owner\": \"string\", \"dueDate\": \"YYYY-MM-DD\"}], " +
                "\"openQuestions\": [{\"questionText\": \"string\"}]}";

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", transcript)
                ),
                "response_format", Map.of("type", "json_object")
        );

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    gatewayUrl + "/v1/chat/completions",
                    new HttpEntity<>(requestBody, headers),
                    Map.class
            );

            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");
                    return objectMapper.readValue(content, AnalysisResult.class);
                }
            }
        } catch (Exception e) {
            log.error("Failed to analyze transcript via LiteLLM", e);
        }

        return new AnalysisResult();
    }

    public float[] generateEmbedding(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", "text-embedding-ada-002",
                "input", text
        );

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    gatewayUrl + "/v1/embeddings",
                    new HttpEntity<>(requestBody, headers),
                    Map.class
            );

            if (response != null && response.containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
                if (!data.isEmpty()) {
                    List<Double> embedding = (List<Double>) data.get(0).get("embedding");
                    float[] result = new float[embedding.size()];
                    for (int i = 0; i < embedding.size(); i++) {
                        result[i] = embedding.get(i).floatValue();
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            log.error("Failed to generate embedding", e);
        }

        return new float[1536];
    }
}
