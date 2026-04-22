package com.microsaas.ecosystemmap.repository;

import com.microsaas.ecosystemmap.entity.RoiMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoiMetricRepository extends JpaRepository<RoiMetric, UUID> {
    List<RoiMetric> findByTenantIdAndEcosystemId(String tenantId, UUID ecosystemId);
    Optional<RoiMetric> findByIdAndTenantId(UUID id, String tenantId);
}
