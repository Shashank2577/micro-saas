package com.crosscutting.socialintelligence.service;

import com.crosscutting.socialintelligence.domain.EngagementMetric;
import com.crosscutting.socialintelligence.dto.UnifiedMetrics;
import com.crosscutting.socialintelligence.repository.EngagementMetricRepository;
import com.crosscutting.socialintelligence.repository.PlatformAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SocialMetricsAggregatorTest {

    @Mock
    private EngagementMetricRepository engagementMetricRepository;

    @Mock
    private PlatformAccountRepository platformAccountRepository;

    @InjectMocks
    private SocialMetricsAggregator socialMetricsAggregator;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetUnifiedMetrics() {
        String tenantId = "test-tenant";
        LocalDate start = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now();

        when(engagementMetricRepository.findByTenantIdAndMetricDateBetween(eq(tenantId), eq(start), eq(end)))
                .thenReturn(List.of(new EngagementMetric()));

        UnifiedMetrics result = socialMetricsAggregator.getUnifiedMetrics(tenantId, start, end);
        assertNotNull(result);
    }
}
