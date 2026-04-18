package com.microsaas.cacheoptimizer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CacheOptimizerConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
