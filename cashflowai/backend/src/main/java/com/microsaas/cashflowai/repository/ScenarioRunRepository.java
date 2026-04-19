package com.microsaas.cashflowai.repository;

import com.microsaas.cashflowai.model.ScenarioRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScenarioRunRepository extends JpaRepository<ScenarioRun, UUID> {
    List<ScenarioRun> findByTenantId(UUID tenantId);
    Optional<ScenarioRun> findByIdAndTenantId(UUID id, UUID tenantId);
}
