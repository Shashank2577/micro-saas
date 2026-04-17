package com.microsaas.pricingintelligence.repository;

import com.microsaas.pricingintelligence.domain.ExperimentResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExperimentResultRepository extends JpaRepository<ExperimentResult, UUID> {
    List<ExperimentResult> findByTenantId(UUID tenantId);
    List<ExperimentResult> findByTenantIdAndExperimentId(UUID tenantId, UUID experimentId);
}
