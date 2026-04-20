package com.microsaas.revopsai.repository;

import com.microsaas.revopsai.model.PipelineMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PipelineMetricRepository extends JpaRepository<PipelineMetric, UUID> {
    List<PipelineMetric> findByTenantId(UUID tenantId);
}
