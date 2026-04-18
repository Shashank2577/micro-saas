package com.microsaas.identitycore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.List;

@Service
public class AIAnalysisService {

    @Value("${cc.ai.gateway-url:http://localhost:4000}")
    private String litellmUrl;

    @Value("${cc.ai.api-key:sk-local-dev-key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analyzeLogsForAnomalies(String logsJson) {
        String prompt = "Analyze these access logs and identify any anomalies (e.g., UNUSUAL_TIME, EXCESSIVE_ACCESS, LATERAL_MOVEMENT). Return a JSON array of anomalies with properties: user_id, access_log_id, anomaly_type, severity (HIGH/MEDIUM/LOW), and description. Logs: " + logsJson;
        return callLiteLLM(prompt);
    }

    public String generateReviewRecommendation(String userJson, String privilegesJson, String logsJson) {
        String prompt = "Review this user's privileges and access history. Recommend actions for each privilege (KEEP, REVOKE, MODIFY). Return a JSON object containing the recommendations and an explanation. User: " + userJson + " Privileges: " + privilegesJson + " Logs: " + logsJson;
        return callLiteLLM(prompt);
    }

    public String generateHygieneReport(String systemStatsJson) {
        String prompt = "Write an identity hygiene report for security leadership based on these stats: " + systemStatsJson + ". Summarize overall health, overprivileged accounts, and open anomalies.";
        return callLiteLLM(prompt);
    }

    private String callLiteLLM(String prompt) {
        int maxRetries = 3;
        int currentAttempt = 0;
        
        while (currentAttempt < maxRetries) {
            try {
                Map<String, Object> requestBody = Map.of(
                        "model", "claude-sonnet-4-6",
                        "messages", List.of(Map.of("role", "user", "content", prompt))
                );
                String body = objectMapper.writeValueAsString(requestBody);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(litellmUrl + "/v1/chat/completions"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + apiKey)
                        .timeout(Duration.ofSeconds(10))
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();

                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() == 200) {
                     var responseMap = objectMapper.readValue(response.body(), Map.class);
                     var choices = (List<Map<String, Object>>) responseMap.get("choices");
                     var message = (Map<String, Object>) choices.get(0).get("message");
                     return (String) message.get("content");
                } else if (response.statusCode() >= 500) {
                    currentAttempt++;
                } else {
                    return "{}";
                }

            } catch (Exception e) {
                currentAttempt++;
            }
        }
        return "{}";
    }
}
