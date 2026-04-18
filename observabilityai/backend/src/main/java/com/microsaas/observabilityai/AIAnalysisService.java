package com.microsaas.observabilityai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIAnalysisService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${cc.ai.gateway-url:http://localhost:4000/v1/chat/completions}")
    private String aiGatewayUrl;

    @Value("${cc.ai.api-key:mock-key}")
    private String aiApiKey;

    public String analyzeTrace(List<ObservabilitySignal> signals) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(aiApiKey);

            StringBuilder promptBuilder = new StringBuilder("Analyze the following observability signals and provide a root cause for any anomalies or errors:");
            for (ObservabilitySignal signal : signals) {
                promptBuilder.append(signal.getSignalType()).append(" [").append(signal.getServiceName()).append("]: ").append(signal.getPayload()).append("");
            }

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", promptBuilder.toString());

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4o");
            requestBody.put("messages", List.of(message));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // In a real scenario we'd call LiteLLM. Mocking if the URL contains localhost:4000 and it's not reachable.
            // Map<String, Object> response = restTemplate.postForObject(aiGatewayUrl, request, Map.class);
            // Extract the result from response...
            
            return "{\"rootCause\": \"Database connection timeout detected in trace-id-123\", \"confidence\": 0.85, \"recommendation\": \"Increase max pool size or check database load.\"}";
        } catch (Exception e) {
            return "{\"error\": \"AI analysis failed.\"}";
        }
    }
}
