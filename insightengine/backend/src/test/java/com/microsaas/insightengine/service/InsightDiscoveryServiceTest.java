package com.microsaas.insightengine.service;

import com.microsaas.insightengine.dto.InsightEnrichment;
import com.microsaas.insightengine.entity.Insight;
import com.microsaas.insightengine.entity.MetricData;
import com.microsaas.insightengine.repository.InsightRepository;
import com.microsaas.insightengine.repository.MetricDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InsightDiscoveryServiceTest {

    @Mock
    private MetricDataRepository metricDataRepository;

    @Mock
    private InsightRepository insightRepository;

    @Mock
    private AiExplanationService aiExplanationService;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private InsightDiscoveryService insightDiscoveryService;

    private UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDiscoverAnomalies() {
        MetricData metric = new MetricData();
        metric.setMetricName("revenue");
        metric.setMetricValue(1000.0);
        
        when(metricDataRepository.findByTenantIdAndTimestampAfter(eq(tenantId), any())).thenReturn(List.of(metric));
        
        InsightEnrichment enrichment = new InsightEnrichment();
        enrichment.setExplanation("Test explanation");
        enrichment.setRecommendedAction("Test action");
        when(aiExplanationService.generateExplanationAndRecommendation(any())).thenReturn(enrichment);
        
        when(insightRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Insight> insights = insightDiscoveryService.discoverAnomalies(tenantId, LocalDateTime.now().minusDays(1));
        
        assertEquals(1, insights.size());
        Insight insight = insights.get(0);
        assertEquals("ANOMALY", insight.getType());
        assertEquals("Test explanation", insight.getExplanation());
        
        verify(insightRepository).save(any());
        verify(alertService).evaluateAlertRules(any());
    }
}
