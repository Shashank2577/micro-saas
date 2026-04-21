package com.crosscutting.socialintelligence;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.crosscutting.starter.webhooks.WebhookService;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.crosscutting.socialintelligence.repository.PlatformAccountRepository;
import com.crosscutting.socialintelligence.repository.EngagementMetricRepository;
import com.crosscutting.socialintelligence.repository.AudienceDemographicRepository;
import com.crosscutting.socialintelligence.repository.ContentAnalysisRepository;
import com.crosscutting.socialintelligence.repository.GrowthRecommendationRepository;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@org.junit.jupiter.api.Disabled
@TestPropertySource(properties = {
    "cc.tenancy.enabled=false",
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=update",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=sa"
})
class SocialIntelligenceApplicationTests {
    @MockBean private PlatformAccountRepository platformAccountRepository;
    @MockBean private WebhookService webhookService;
    @MockBean private EngagementMetricRepository engagementMetricRepository;
    @MockBean private AudienceDemographicRepository audienceDemographicRepository;
    @MockBean private ContentAnalysisRepository contentAnalysisRepository;
    @MockBean private GrowthRecommendationRepository growthRecommendationRepository;


    @Test
    void contextLoads() {
    }

}
