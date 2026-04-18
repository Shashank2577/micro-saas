package com.microsaas.agencyos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LiteLLMClient {

    private final RestTemplate restTemplate;
    
    @Value("${cc.ai.gateway-url}")
    private String gatewayUrl;
    
    @Value("${cc.ai.api-key}")
    private String apiKey;
    
    public LiteLLMClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    // Stub implementation for LiteLLM
    public String getCompletion(String prompt) {
        // Implementation would use gatewayUrl and apiKey
        return "Stub response for: " + prompt;
    }
}
