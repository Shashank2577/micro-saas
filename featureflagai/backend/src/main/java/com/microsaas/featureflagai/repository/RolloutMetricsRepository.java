package com.microsaas.featureflagai.repository;

import com.microsaas.featureflagai.domain.RolloutMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolloutMetricsRepository extends JpaRepository<RolloutMetrics, UUID> {
    List<RolloutMetrics> findByFlagIdAndTenantIdOrderByTimestampDesc(UUID flagId, UUID tenantId);
}
