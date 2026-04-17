package com.microsaas.backupvault.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BackupVaultConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
