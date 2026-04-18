package com.ecosystemmap.service;

import com.ecosystemmap.domain.ValueMetric;
import com.ecosystemmap.repository.ValueMetricRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MetricsServiceTest {

    @Mock
    private ValueMetricRepository valueMetricRepository;

    @InjectMocks
    private MetricsService metricsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMetrics() {
        UUID tenantId = UUID.randomUUID();
        when(valueMetricRepository.findTop5ByTenantIdOrderByCalculatedAtDesc(tenantId))
                .thenReturn(List.of(new ValueMetric()));

        List<ValueMetric> result = metricsService.getMetrics(tenantId);
        assertEquals(1, result.size());
    }

    @Test
    void testCalculateCompoundValue() {
        UUID tenantId = UUID.randomUUID();
        when(valueMetricRepository.save(any(ValueMetric.class))).thenAnswer(i -> i.getArguments()[0]);

        ValueMetric result = metricsService.calculateCompoundValue(tenantId);
        assertNotNull(result);
        assertEquals(new BigDecimal("120.50"), result.getMetricValue());
        assertEquals("Hours Saved", result.getMetricName());
    }
}
