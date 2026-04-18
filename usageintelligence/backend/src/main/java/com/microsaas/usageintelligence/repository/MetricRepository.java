package com.microsaas.usageintelligence.repository;

import com.microsaas.usageintelligence.domain.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MetricRepository extends JpaRepository<Metric, UUID> {
    List<Metric> findByTenantId(UUID tenantId);
    List<Metric> findByTenantIdAndMetricNameAndCreatedAtBetween(UUID tenantId, String metricName, LocalDateTime startDate, LocalDateTime endDate);
}
