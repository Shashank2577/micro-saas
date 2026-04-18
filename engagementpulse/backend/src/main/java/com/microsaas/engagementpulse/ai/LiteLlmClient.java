package com.microsaas.engagementpulse.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.engagementpulse.config.LiteLlmConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LiteLlmClient {

    private final RestTemplate restTemplate;
    private final LiteLlmConfig config;
    private final ObjectMapper objectMapper;

    @Autowired
    public LiteLlmClient(RestTemplate restTemplate, LiteLlmConfig config, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public SentimentResult analyzeSentiment(String text) {
        String url = config.getBaseUrl() + "/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(config.getApiKey());

        String prompt = "Analyze the following employee feedback and classify its sentiment as POSITIVE, NEUTRAL, or NEGATIVE. Also provide a score from -1.0 to 1.0. Respond in strictly JSON format with keys 'label' and 'score'. Feedback: '" + text + "'";

        Map<String, Object> body = new HashMap<>();
        body.put("model", config.getModel());
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");
                    JsonNode node = objectMapper.readTree(content);
                    String label = node.has("label") ? node.get("label").asText() : "NEUTRAL";
                    double score = node.has("score") ? node.get("score").asDouble() : 0.0;
                    return new SentimentResult(label, score);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SentimentResult("NEUTRAL", 0.0);
    }
    
    public static class SentimentResult {
        public String label;
        public Double score;
        public SentimentResult(String label, Double score) {
            this.label = label;
            this.score = score;
        }
    }
}
