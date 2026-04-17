package com.microsaas.pricingintelligence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PricingIntelligenceConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
