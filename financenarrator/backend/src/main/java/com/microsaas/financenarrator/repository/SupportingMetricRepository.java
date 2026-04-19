package com.microsaas.financenarrator.repository;

import com.microsaas.financenarrator.model.SupportingMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupportingMetricRepository extends JpaRepository<SupportingMetric, UUID> {
    List<SupportingMetric> findByTenantId(UUID tenantId);
    Optional<SupportingMetric> findByIdAndTenantId(UUID id, UUID tenantId);
}
