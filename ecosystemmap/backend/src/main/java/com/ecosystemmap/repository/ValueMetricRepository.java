package com.ecosystemmap.repository;

import com.ecosystemmap.domain.ValueMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ValueMetricRepository extends JpaRepository<ValueMetric, UUID> {
    List<ValueMetric> findByTenantId(UUID tenantId);
    List<ValueMetric> findTop5ByTenantIdOrderByCalculatedAtDesc(UUID tenantId);
}
