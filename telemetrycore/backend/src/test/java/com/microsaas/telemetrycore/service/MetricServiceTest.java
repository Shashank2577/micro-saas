package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.model.Metric;
import com.microsaas.telemetrycore.model.Event;
import com.microsaas.telemetrycore.repository.MetricRepository;
import com.microsaas.telemetrycore.repository.EventRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetricServiceTest {

    @Mock
    private MetricRepository metricRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private MetricAggregator metricAggregator;

    @InjectMocks
    private MetricService metricService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testRecordMetric_success() {
        Metric metric = new Metric();
        metric.setName("cpu_usage");

        when(metricRepository.save(any(Metric.class))).thenAnswer(invocation -> {
            Metric saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        Metric result = metricService.createMetric(metric);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertNotNull(result.getCreatedAt());
        verify(metricRepository, times(1)).save(any(Metric.class));
    }

    @Test
    void testGetMetrics_byTenantAndTimeRange() {
        UUID metricId = UUID.randomUUID();
        ZonedDateTime start = ZonedDateTime.now().minusHours(1);
        ZonedDateTime end = ZonedDateTime.now();

        Metric metric = new Metric();
        metric.setId(metricId);
        metric.setAggregationType("sum");

        when(metricRepository.findByIdAndTenantId(metricId, tenantId)).thenReturn(Optional.of(metric));

        List<Event> expectedEvents = Arrays.asList(new Event());
        when(eventRepository.findByTenantIdAndTimestampBetween(tenantId, start, end)).thenReturn(expectedEvents);

        when(metricAggregator.aggregateEvents(expectedEvents, "sum")).thenReturn(new java.util.HashMap<>());

        List<Map<String, Object>> result = metricService.getMetricData(metricId, start, end, "1h");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testAggregateMetrics_sum() {
        // Since MetricService's getMetricData serves the purpose of aggregating metrics,
        // let's test it using sum aggregation type.
        UUID metricId = UUID.randomUUID();
        ZonedDateTime start = ZonedDateTime.now().minusHours(1);
        ZonedDateTime end = ZonedDateTime.now();

        Metric metric = new Metric();
        metric.setId(metricId);
        metric.setAggregationType("sum");

        when(metricRepository.findByIdAndTenantId(metricId, tenantId)).thenReturn(Optional.of(metric));

        List<Event> expectedEvents = Arrays.asList(new Event(), new Event());
        when(eventRepository.findByTenantIdAndTimestampBetween(tenantId, start, end)).thenReturn(expectedEvents);

        Map<String, Object> aggregatedData = new java.util.HashMap<>();
        aggregatedData.put("value", 100.0);
        when(metricAggregator.aggregateEvents(expectedEvents, "sum")).thenReturn(aggregatedData);

        List<Map<String, Object>> result = metricService.getMetricData(metricId, start, end, "1h");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100.0, result.get(0).get("value"));
    }

    @Test
    void testAggregateMetrics_p99() {
        UUID metricId = UUID.randomUUID();
        ZonedDateTime start = ZonedDateTime.now().minusHours(1);
        ZonedDateTime end = ZonedDateTime.now();

        Metric metric = new Metric();
        metric.setId(metricId);
        metric.setAggregationType("p99");

        when(metricRepository.findByIdAndTenantId(metricId, tenantId)).thenReturn(Optional.of(metric));

        List<Event> expectedEvents = Arrays.asList(new Event(), new Event());
        when(eventRepository.findByTenantIdAndTimestampBetween(tenantId, start, end)).thenReturn(expectedEvents);

        Map<String, Object> aggregatedData = new java.util.HashMap<>();
        aggregatedData.put("value", 95.5);
        when(metricAggregator.aggregateEvents(expectedEvents, "p99")).thenReturn(aggregatedData);

        List<Map<String, Object>> result = metricService.getMetricData(metricId, start, end, "1h");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(95.5, result.get(0).get("value"));
    }
}
