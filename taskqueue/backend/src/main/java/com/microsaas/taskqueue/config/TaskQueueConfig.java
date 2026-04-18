package com.microsaas.taskqueue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TaskQueueConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
