package com.ecosystemmap.service;

import com.ecosystemmap.domain.ValueMetric;
import com.ecosystemmap.repository.ValueMetricRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service

public class MetricsService {

    private final ValueMetricRepository valueMetricRepository;
        @org.springframework.beans.factory.annotation.Autowired
    public MetricsService(ValueMetricRepository valueMetricRepository) {
        this.valueMetricRepository = valueMetricRepository;
    }

    public List<ValueMetric> getMetrics(UUID tenantId) {
        return valueMetricRepository.findTop5ByTenantIdOrderByCalculatedAtDesc(tenantId);
    }

    @Transactional
    public ValueMetric calculateCompoundValue(UUID tenantId) {
        // Dummy calculation for compound value
        ValueMetric metric = new ValueMetric();
        metric.setId(UUID.randomUUID());
        metric.setTenantId(tenantId);
        metric.setMetricName("Hours Saved");
        metric.setMetricValue(new BigDecimal("120.50"));
        metric.setCalculatedAt(LocalDateTime.now());
        metric.setDescription("Estimated hours saved by automated ecosystem flows.");
        metric.setCreatedAt(LocalDateTime.now());
        metric.setUpdatedAt(LocalDateTime.now());
        
        return valueMetricRepository.save(metric);
    }
}
