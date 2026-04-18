package com.microsaas.engagementpulse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EngagementPulseConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
