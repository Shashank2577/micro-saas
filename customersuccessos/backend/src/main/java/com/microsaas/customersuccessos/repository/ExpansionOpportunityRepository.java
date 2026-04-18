package com.microsaas.customersuccessos.repository;

import com.microsaas.customersuccessos.model.ExpansionOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface ExpansionOpportunityRepository extends JpaRepository<ExpansionOpportunity, UUID> {
    List<ExpansionOpportunity> findByTenantIdAndAccountId(UUID tenantId, UUID accountId);
    List<ExpansionOpportunity> findByTenantId(UUID tenantId);
}
