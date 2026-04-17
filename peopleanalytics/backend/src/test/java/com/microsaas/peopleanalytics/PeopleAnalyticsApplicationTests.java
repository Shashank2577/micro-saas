package com.microsaas.peopleanalytics;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.webhooks.WebhookService;
import io.minio.MinioClient;

@SpringBootTest
@ActiveProfiles("test")
class PeopleAnalyticsApplicationTests {

    @MockBean
    private AiService aiService;

    @MockBean
    private MinioClient minioClient;

    @MockBean
    private WebhookService webhookService;

    @Test
    void contextLoads() {
    }

}
