package com.microsaas.creatoranalytics.repository;

import com.microsaas.creatoranalytics.model.PerformanceInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerformanceInsightRepository extends JpaRepository<PerformanceInsight, UUID> {
    List<PerformanceInsight> findByTenantId(UUID tenantId);
    Optional<PerformanceInsight> findByIdAndTenantId(UUID id, UUID tenantId);
}
