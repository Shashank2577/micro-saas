package com.microsaas.processminer.repository;

import com.microsaas.processminer.domain.AutomationOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AutomationOpportunityRepository extends JpaRepository<AutomationOpportunity, UUID> {
    List<AutomationOpportunity> findByTenantId(UUID tenantId);
    List<AutomationOpportunity> findByTenantIdAndProcessModelId(UUID tenantId, UUID processModelId);
    void deleteByTenantIdAndProcessModelId(UUID tenantId, UUID processModelId);
}
