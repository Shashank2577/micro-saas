package com.microsaas.contractportfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ContractPortfolioConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
