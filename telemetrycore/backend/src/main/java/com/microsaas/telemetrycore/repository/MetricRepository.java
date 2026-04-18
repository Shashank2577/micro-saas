package com.microsaas.telemetrycore.repository;

import com.microsaas.telemetrycore.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MetricRepository extends JpaRepository<Metric, UUID> {
    List<Metric> findByTenantId(UUID tenantId);
    Optional<Metric> findByIdAndTenantId(UUID id, UUID tenantId);
}
