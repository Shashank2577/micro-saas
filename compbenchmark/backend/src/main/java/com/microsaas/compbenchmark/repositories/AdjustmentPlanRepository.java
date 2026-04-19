package com.microsaas.compbenchmark.repositories;

import com.microsaas.compbenchmark.model.AdjustmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdjustmentPlanRepository extends JpaRepository<AdjustmentPlan, UUID> {
    List<AdjustmentPlan> findByTenantId(UUID tenantId);
    Optional<AdjustmentPlan> findByIdAndTenantId(UUID id, UUID tenantId);
}
