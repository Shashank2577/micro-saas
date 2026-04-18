package com.microsaas.telemetrycore.service;

import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AiAnomalyService {
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${cc.ai.gateway-url:http://litellm:4000}")
    private String litellmUrl;

    @Value("${cc.ai.api-key:dummy-key}")
    private String litellmKey;

    public String analyzeMetricAnomaly(String metricName, List<Map<String, Object>> recentData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(litellmKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are an AI trained to detect anomalies in telemetry time-series data."),
                Map.of("role", "user", "content", "Analyze this recent metric data for " + metricName + " and detect any anomalies: " + recentData.toString())
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(litellmUrl + "/v1/chat/completions", request, Map.class);
            if (response.getBody() != null && response.getBody().containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            return "Unable to analyze data at this time: " + e.getMessage();
        }
        return "No anomaly detected.";
    }
}
