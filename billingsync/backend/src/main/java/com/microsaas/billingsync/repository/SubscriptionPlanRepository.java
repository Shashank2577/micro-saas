package com.microsaas.billingsync.repository;

import com.microsaas.billingsync.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, UUID> {
    List<SubscriptionPlan> findByTenantId(String tenantId);
    Optional<SubscriptionPlan> findByIdAndTenantId(UUID id, String tenantId);
}
