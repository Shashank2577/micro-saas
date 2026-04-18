package com.microsaas.billingsync.repository;

import com.microsaas.billingsync.model.PricingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PricingModelRepository extends JpaRepository<PricingModel, UUID> {
    List<PricingModel> findByTenantId(String tenantId);
    List<PricingModel> findByPlanIdAndTenantId(UUID planId, String tenantId);
}
