package com.microsaas.auditvault.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AuditVaultConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
