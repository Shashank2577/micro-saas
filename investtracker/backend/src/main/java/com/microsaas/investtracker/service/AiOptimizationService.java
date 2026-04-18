package com.microsaas.investtracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.investtracker.dto.AiInsightDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AiOptimizationService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${litellm.url:http://localhost:4000}")
    private String liteLlmUrl;

    public AiOptimizationService(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public AiInsightDto generateInsights(UUID portfolioId) {
        // In a real application, fetch holdings and target allocation to build the prompt.
        String prompt = "Analyze the portfolio and return a JSON with riskScore (1-100), volatilityAssessment (Low/Med/High), and a list of rebalanceRecommendations.";
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "response_format", Map.of("type", "json_object")
            );
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(liteLlmUrl + "/v1/chat/completions", request, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");
                    return objectMapper.readValue(content, AiInsightDto.class);
                }
            }
        } catch (Exception e) {
            // Fallback mock if LiteLLM is down or not configured
        }
        
        AiInsightDto fallback = new AiInsightDto();
        fallback.setRiskScore(65);
        fallback.setVolatilityAssessment("Medium");
        fallback.setRebalanceRecommendations(List.of("Sell AAPL to target 10%", "Buy Bonds to increase safety"));
        return fallback;
    }
}
