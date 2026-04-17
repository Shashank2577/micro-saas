package com.microsaas.dataqualityai.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class AiService {
    @Value("${litellm.url:http://localhost:4000}")
    private String litellmUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String suggestFix(String issueDescription, String observedJson) {
        String prompt = "Data Quality Issue Description: " + issueDescription +
                        "\nObserved Data: " + observedJson +
                        "\nSuggest a SQL fix or investigation steps to remediate this issue. Keep it actionable and concise.";

        Map<String, Object> request = new HashMap<>();
        request.put("model", "claude-3-5-sonnet-20241022");
        request.put("messages", List.of(
            Map.of("role", "system", "content", "You are an expert data engineer analyzing data quality test failures."),
            Map.of("role", "user", "content", prompt)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(litellmUrl + "/v1/chat/completions", entity, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            System.err.println("Error calling LiteLLM: " + e.getMessage());
            return "Failed to generate suggestion. Please review the issue manually.";
        }

        return "No suggestion could be generated.";
    }
}
