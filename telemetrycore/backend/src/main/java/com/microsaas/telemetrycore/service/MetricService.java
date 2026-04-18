package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.model.Metric;
import com.microsaas.telemetrycore.model.Event;
import com.microsaas.telemetrycore.repository.MetricRepository;
import com.microsaas.telemetrycore.repository.EventRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MetricService {
    @Autowired
    private MetricRepository metricRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MetricAggregator metricAggregator;

    @Transactional
    public Metric createMetric(Metric metric) {
        metric.setTenantId(TenantContext.require());
        metric.setCreatedAt(ZonedDateTime.now());
        metric.setUpdatedAt(ZonedDateTime.now());
        return metricRepository.save(metric);
    }
    
    @Transactional(readOnly = true)
    public List<Metric> getAllMetrics() {
        return metricRepository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Metric getMetric(UUID id) {
        return metricRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Metric not found"));
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMetricData(UUID metricId, ZonedDateTime startDate, ZonedDateTime endDate, String interval) {
        Metric metric = getMetric(metricId);
        List<Event> events = eventRepository.findByTenantIdAndTimestampBetween(TenantContext.require(), startDate, endDate);
        
        Map<String, Object> dataPoint = metricAggregator.aggregateEvents(events, metric.getAggregationType());
        dataPoint.put("timestamp", endDate);
        
        return List.of(dataPoint);
    }
}
