package com.microsaas.datagovernance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DataGovernanceConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
