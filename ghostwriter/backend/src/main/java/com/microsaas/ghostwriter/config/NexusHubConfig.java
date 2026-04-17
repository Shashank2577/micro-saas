package com.microsaas.ghostwriter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GhostWriterConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
