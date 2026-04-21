package com.microsaas.budgetpilot;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.queue.QueueService;
import com.crosscutting.starter.storage.StorageService;
import com.crosscutting.starter.payments.PaymentService;
import com.crosscutting.starter.webhooks.WebhookService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
@Profile("test")
public class MockConfig {
    @Bean
    @Primary
    public AiService aiService() {
        return Mockito.mock(AiService.class);
    }

    @Bean
    @Primary
    public QueueService queueService() {
        return Mockito.mock(QueueService.class);
    }

    @Bean
    @Primary
    public StorageService storageService() {
        return Mockito.mock(StorageService.class);
    }

    @Bean
    @Primary
    public PaymentService paymentService() {
        return Mockito.mock(PaymentService.class);
    }

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
