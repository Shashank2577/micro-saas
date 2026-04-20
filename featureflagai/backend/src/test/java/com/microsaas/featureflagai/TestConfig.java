package com.microsaas.featureflagai;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.webhooks.WebhookService;
import com.crosscutting.starter.storage.StorageService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public AiService aiService() {
        return Mockito.mock(AiService.class);
    }

    @Bean
    public WebhookService webhookService() {
        return Mockito.mock(WebhookService.class);
    }

    @Bean
    public StorageService storageService() {
        return Mockito.mock(StorageService.class);
    }
}
