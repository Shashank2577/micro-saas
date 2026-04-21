package com.microsaas.dealbrain.repository;

import com.microsaas.dealbrain.model.SalesStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SalesStrategyRepository extends JpaRepository<SalesStrategy, UUID> {
    List<SalesStrategy> findByTenantIdAndDealId(UUID tenantId, UUID dealId);
}
