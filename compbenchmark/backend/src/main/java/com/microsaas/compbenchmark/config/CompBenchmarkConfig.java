package com.microsaas.compbenchmark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CompBenchmarkConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
