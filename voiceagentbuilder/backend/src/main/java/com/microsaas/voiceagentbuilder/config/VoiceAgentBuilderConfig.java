package com.microsaas.voiceagentbuilder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class VoiceAgentBuilderConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
