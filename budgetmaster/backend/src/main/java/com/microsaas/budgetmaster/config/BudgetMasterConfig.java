package com.microsaas.budgetmaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BudgetMasterConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
