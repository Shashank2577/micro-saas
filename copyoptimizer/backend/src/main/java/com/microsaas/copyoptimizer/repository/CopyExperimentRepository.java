package com.microsaas.copyoptimizer.repository;

import com.microsaas.copyoptimizer.model.CopyExperiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CopyExperimentRepository extends JpaRepository<CopyExperiment, UUID> {
    List<CopyExperiment> findByTenantId(UUID tenantId);
    Optional<CopyExperiment> findByIdAndTenantId(UUID id, UUID tenantId);
}
