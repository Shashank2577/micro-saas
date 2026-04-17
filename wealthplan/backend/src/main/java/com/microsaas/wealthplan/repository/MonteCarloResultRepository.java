package com.microsaas.wealthplan.repository;

import com.microsaas.wealthplan.entity.MonteCarloResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MonteCarloResultRepository extends JpaRepository<MonteCarloResult, UUID> {
    List<MonteCarloResult> findByTenantIdAndScenarioIdOrderBySimulationDateDesc(String tenantId, UUID scenarioId);
}
