package com.microsaas.experimentengine.domain.repository;

import com.microsaas.experimentengine.domain.model.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, UUID> {
    List<Experiment> findByTenantId(UUID tenantId);
    Optional<Experiment> findByIdAndTenantId(UUID id, UUID tenantId);
}
