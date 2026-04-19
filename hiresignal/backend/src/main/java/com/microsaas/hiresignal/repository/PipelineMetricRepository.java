package com.microsaas.hiresignal.repository;

import com.microsaas.hiresignal.model.PipelineMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PipelineMetricRepository extends JpaRepository<PipelineMetric, UUID> {
    List<PipelineMetric> findByTenantId(UUID tenantId);
    Optional<PipelineMetric> findByIdAndTenantId(UUID id, UUID tenantId);
}
