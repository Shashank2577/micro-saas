package com.microsaas.telemetrycore.repository;

import com.microsaas.telemetrycore.model.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExperimentRepository extends JpaRepository<Experiment, UUID> {
    List<Experiment> findByTenantId(UUID tenantId);
    Optional<Experiment> findByIdAndTenantId(UUID id, UUID tenantId);
}
