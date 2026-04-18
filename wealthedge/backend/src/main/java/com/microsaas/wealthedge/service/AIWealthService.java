package com.microsaas.wealthedge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIWealthService {

    @Value("${cc.ai.gateway-url:http://localhost:4000}")
    private String gatewayUrl;

    @Value("${cc.ai.api-key:test-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analyzeRiskConcentration(String portfolioData) {
        String prompt = "Analyze the following wealth portfolio data and provide insights on risk concentration, tax planning, and alternative investments strategy: " + portfolioData;

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-4");
        request.put("messages", List.of(
            Map.of("role", "system", "content", "You are an expert wealth manager and financial advisor."),
            Map.of("role", "user", "content", prompt)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                Map<String, Object> response = restTemplate.postForObject(gatewayUrl + "/v1/chat/completions", entity, Map.class);
                if (response != null && response.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    if (!choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return (String) message.get("content");
                    }
                }
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    return "AI Analysis temporarily unavailable: " + e.getMessage();
                }
                try { Thread.sleep((long) Math.pow(2, i) * 1000); } catch (InterruptedException ignored) {}
            }
        }
        return "No insights generated.";
    }
}
