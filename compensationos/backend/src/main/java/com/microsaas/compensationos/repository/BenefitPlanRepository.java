package com.microsaas.compensationos.repository;

import com.microsaas.compensationos.entity.BenefitPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BenefitPlanRepository extends JpaRepository<BenefitPlan, UUID> {
    List<BenefitPlan> findByTenantId(UUID tenantId);
}
