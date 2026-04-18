package com.microsaas.legalresearch;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.webhooks.WebhookService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"cc.tenancy.enabled=false", "spring.flyway.enabled=false"})
class LegalResearchApplicationTests {

    @MockBean
    private AiService aiService;

    @MockBean
    private WebhookService webhookService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void contextLoads() {
    }
}
