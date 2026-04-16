package com.microsaas.policyforge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PolicyForgeConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
