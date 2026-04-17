package com.microsaas.documentintelligence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DocumentIntelligenceConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
