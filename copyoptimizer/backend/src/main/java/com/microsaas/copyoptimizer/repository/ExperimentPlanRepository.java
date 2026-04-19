package com.microsaas.copyoptimizer.repository;

import com.microsaas.copyoptimizer.model.ExperimentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExperimentPlanRepository extends JpaRepository<ExperimentPlan, UUID> {
    List<ExperimentPlan> findByTenantId(UUID tenantId);
    Optional<ExperimentPlan> findByIdAndTenantId(UUID id, UUID tenantId);
}
