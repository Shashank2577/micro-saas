package com.microsaas.prospectiq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProspectIQConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
