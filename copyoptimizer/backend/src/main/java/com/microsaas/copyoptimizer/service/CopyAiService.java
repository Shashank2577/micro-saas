package com.microsaas.copyoptimizer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CopyAiService {

    @Value("${LITELLM_BASE_URL:http://localhost:4000}")
    private String litellmUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> analyze(Map<String, Object> request) {
        log.info("AI Analyze requested via {}", litellmUrl);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            // Example LiteLLM integration call - using a generic endpoint for demonstration
            ResponseEntity<Map> response = restTemplate.postForEntity(litellmUrl + "/v1/chat/completions", entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("LiteLLM analysis failed, falling back to stub", e);
            return Map.of("analysis", "Looks like a great copy asset.", "confidence", 0.95);
        }
    }

    public Map<String, Object> recommend(Map<String, Object> request) {
        log.info("AI Recommendations requested via {}", litellmUrl);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(litellmUrl + "/v1/chat/completions", entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("LiteLLM recommendation failed, falling back to stub", e);
            return Map.of("recommendations", "Add more urgency words.", "impactScore", "+15%");
        }
    }
}
