package com.microsaas.telemetrycore.repository;

import com.microsaas.telemetrycore.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CohortRepository extends JpaRepository<Cohort, UUID> {
    List<Cohort> findByTenantId(UUID tenantId);
    Optional<Cohort> findByIdAndTenantId(UUID id, UUID tenantId);
}
