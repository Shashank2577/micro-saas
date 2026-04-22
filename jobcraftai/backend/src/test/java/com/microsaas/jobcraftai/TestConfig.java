package com.microsaas.jobcraftai;

import com.crosscutting.starter.webhooks.WebhookService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public WebhookService webhookService() {
        return Mockito.mock(WebhookService.class);
    }

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        return Mockito.mock(JwtDecoder.class);
    }
}
