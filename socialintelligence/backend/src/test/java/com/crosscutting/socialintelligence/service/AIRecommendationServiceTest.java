package com.crosscutting.socialintelligence.service;

import com.crosscutting.socialintelligence.client.LiteLLMClient;
import com.crosscutting.socialintelligence.domain.GrowthRecommendation;
import com.crosscutting.socialintelligence.dto.UnifiedMetrics;
import com.crosscutting.socialintelligence.repository.EngagementMetricRepository;
import com.crosscutting.socialintelligence.repository.GrowthRecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class AIRecommendationServiceTest {

    @Mock
    private LiteLLMClient liteLLMClient;

    @Mock
    private GrowthRecommendationRepository growthRecommendationRepository;

    @Mock
    private EngagementMetricRepository engagementMetricRepository;

    @Mock
    private SocialMetricsAggregator metricsAggregator;

    @InjectMocks
    private AIRecommendationService aiRecommendationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGenerateRecommendations() {
        String tenantId = "test-tenant";

        when(metricsAggregator.getUnifiedMetrics(eq(tenantId), any(LocalDate.class), any(LocalDate.class))).thenReturn(new UnifiedMetrics());
        when(liteLLMClient.generateCompletion(anyString())).thenReturn("test-recommendation");
        lenient().when(growthRecommendationRepository.saveAll(org.mockito.ArgumentMatchers.anyList()))
                .thenReturn(List.of(new GrowthRecommendation()));

        List<GrowthRecommendation> result = aiRecommendationService.generateRecommendations(tenantId);
        assertNotNull(result);
    }
}
