package com.microsaas.backupvault.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class BackupAIAnalyzerService {

    @Value("${litellm.url:http://localhost:4000}")
    private String litellmUrl;

    @Value("${litellm.api.key:sk-1234}")
    private String litellmApiKey;

    private final RestTemplate restTemplate;

    public BackupAIAnalyzerService() {
        this.restTemplate = new RestTemplate();
    }

    public String suggestOptimization(String currentSchedule, Long avgBackupSizeBytes, Long avgDurationMinutes) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(litellmApiKey);

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", String.format(
                "Analyze backup schedule. Current cron: %s, Avg size: %d bytes, Avg duration: %d min. Suggest an optimized schedule to minimize impact during peak hours (assuming peak is 9 AM - 5 PM).",
                currentSchedule, avgBackupSizeBytes, avgDurationMinutes
            ));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", List.of(message));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(litellmUrl + "/v1/chat/completions", request, Map.class);
            
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, String> msg = (Map<String, String>) firstChoice.get("message");
                    return msg.get("content");
                }
            }
            return "No suggestions at this time.";
        } catch (Exception e) {
            return "AI Optimization temporarily unavailable.";
        }
    }
}
