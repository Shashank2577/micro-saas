package com.microsaas.debtnavigator.repository;

import com.microsaas.debtnavigator.entity.PaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, UUID> {
    List<PaymentPlan> findByTenantId(UUID tenantId);
    Optional<PaymentPlan> findByIdAndTenantId(UUID id, UUID tenantId);
}
