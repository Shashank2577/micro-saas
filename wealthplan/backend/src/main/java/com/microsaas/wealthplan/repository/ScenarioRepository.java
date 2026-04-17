package com.microsaas.wealthplan.repository;

import com.microsaas.wealthplan.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, UUID> {
    List<Scenario> findByTenantIdAndGoalId(String tenantId, UUID goalId);
    Optional<Scenario> findByIdAndTenantId(UUID id, String tenantId);
}
