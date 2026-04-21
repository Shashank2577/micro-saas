package com.microsaas.contextlayer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ContextLayerConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
