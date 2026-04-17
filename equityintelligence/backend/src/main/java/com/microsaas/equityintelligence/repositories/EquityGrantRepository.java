package com.microsaas.equityintelligence.repositories;

import com.microsaas.equityintelligence.model.EquityGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EquityGrantRepository extends JpaRepository<EquityGrant, UUID> {
    List<EquityGrant> findAllByTenantId(UUID tenantId);
    List<EquityGrant> findAllByStakeholderIdAndTenantId(UUID stakeholderId, UUID tenantId);
}
