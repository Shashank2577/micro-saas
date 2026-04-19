package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.PlanningScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanningScenarioRepository extends JpaRepository<PlanningScenario, UUID> {
    List<PlanningScenario> findByTenantId(UUID tenantId);
    Optional<PlanningScenario> findByIdAndTenantId(UUID id, UUID tenantId);
}
