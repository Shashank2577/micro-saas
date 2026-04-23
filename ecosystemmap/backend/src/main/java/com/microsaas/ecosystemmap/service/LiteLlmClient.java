package com.microsaas.ecosystemmap.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class LiteLlmClient {

    @Value("${litellm.api.url:http://litellm:4000}")
    private String apiUrl;

    @Value("${litellm.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public LiteLlmClient() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> chatCompletions(Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        if (apiKey != null && !apiKey.isEmpty()) {
            headers.set("Authorization", "Bearer " + apiKey);
        }

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                apiUrl + "/v1/chat/completions",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        return response.getBody();
    }
}
