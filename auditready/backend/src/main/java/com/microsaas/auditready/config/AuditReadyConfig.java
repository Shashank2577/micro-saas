package com.microsaas.auditready.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AuditReadyConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
