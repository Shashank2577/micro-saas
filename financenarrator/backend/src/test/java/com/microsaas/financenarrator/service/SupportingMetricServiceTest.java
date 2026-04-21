package com.microsaas.financenarrator.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.financenarrator.model.SupportingMetric;
import com.microsaas.financenarrator.repository.SupportingMetricRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupportingMetricServiceTest {

    @Mock
    private SupportingMetricRepository repository;

    @Mock
    private EventPublisherService eventPublisher;

    @InjectMocks
    private SupportingMetricService service;

    private MockedStatic<TenantContext> tenantContextMockedStatic;
    private UUID tenantId;
    private UUID metricId;
    private SupportingMetric metric;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        metricId = UUID.randomUUID();
        tenantContextMockedStatic = Mockito.mockStatic(TenantContext.class);
        tenantContextMockedStatic.when(TenantContext::require).thenReturn(tenantId);

        metric = new SupportingMetric();
        metric.setId(metricId);
        metric.setTenantId(tenantId);
        metric.setName("EBITDA Growth");
        metric.setStatus("DRAFT");
    }

    @AfterEach
    void tearDown() {
        tenantContextMockedStatic.close();
    }

    @Test
    void testAddMetric_toRequest() {
        when(repository.save(any(SupportingMetric.class))).thenReturn(metric);

        SupportingMetric result = service.create(metric);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(repository, times(1)).save(metric);
        verify(eventPublisher, times(1)).publishEvent(
                eq("financenarrator.narrative.generated"),
                eq(tenantId),
                eq("financenarrator"),
                any(Map.class)
        );
    }

    @Test
    void testListMetrics_byRequestId() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(metric));

        List<SupportingMetric> result = service.list();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository, times(1)).findByTenantId(tenantId);
    }
}
