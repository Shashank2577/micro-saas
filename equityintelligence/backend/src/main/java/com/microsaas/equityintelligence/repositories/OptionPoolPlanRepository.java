package com.microsaas.equityintelligence.repositories;

import com.microsaas.equityintelligence.model.OptionPoolPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OptionPoolPlanRepository extends JpaRepository<OptionPoolPlan, UUID> {
    List<OptionPoolPlan> findByTenantId(UUID tenantId);
    Optional<OptionPoolPlan> findByIdAndTenantId(UUID id, UUID tenantId);
}
