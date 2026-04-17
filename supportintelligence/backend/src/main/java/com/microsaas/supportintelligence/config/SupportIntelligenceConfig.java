package com.microsaas.supportintelligence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SupportIntelligenceConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
