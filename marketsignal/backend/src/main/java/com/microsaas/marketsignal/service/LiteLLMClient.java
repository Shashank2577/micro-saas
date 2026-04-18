package com.microsaas.marketsignal.service;

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
public class LiteLLMClient {

    @Value("${litellm.base-url}")
    private String baseUrl;

    @Value("${litellm.api-key}")
    private String apiKey;

    @Value("${litellm.model}")
    private String model;

    @Value("${litellm.embedding-model}")
    private String embeddingModel;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateCompletion(String prompt, String systemInstruction) {
        String url = baseUrl + "/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemInstruction),
                Map.of("role", "user", "content", prompt)
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            // Mock fallback if LiteLLM is not reachable in dev
            return "Generated mock response from LiteLLM due to error: " + e.getMessage();
        }
        return "Generated mock response (empty body)";
    }

    public float[] generateEmbedding(String text) {
        String url = baseUrl + "/embeddings";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", embeddingModel);
        body.put("input", text);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
                if (data != null && !data.isEmpty()) {
                    List<Double> embeddingList = (List<Double>) data.get(0).get("embedding");
                    float[] result = new float[embeddingList.size()];
                    for (int i = 0; i < embeddingList.size(); i++) {
                        result[i] = embeddingList.get(i).floatValue();
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            // Provide a dummy embedding array on failure
            return new float[1536];
        }
        return new float[1536];
    }
}
