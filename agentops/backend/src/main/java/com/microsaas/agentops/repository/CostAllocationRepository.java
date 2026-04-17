package com.microsaas.agentops.repository;

import com.microsaas.agentops.model.CostAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CostAllocationRepository extends JpaRepository<CostAllocation, UUID> {
    List<CostAllocation> findByTenantId(UUID tenantId);
}
