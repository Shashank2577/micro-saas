package com.microsaas.callintelligence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CallIntelligenceConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
