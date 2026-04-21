package com.crosscutting.socialintelligence.repository;

import com.crosscutting.socialintelligence.domain.EngagementMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EngagementMetricRepository extends JpaRepository<EngagementMetric, UUID> {
    List<EngagementMetric> findByTenantId(UUID tenantId);
}
