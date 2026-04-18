package com.ecosystemmap.repository;

import com.ecosystemmap.domain.IntegrationOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface IntegrationOpportunityRepository extends JpaRepository<IntegrationOpportunity, UUID> {
    List<IntegrationOpportunity> findByTenantId(UUID tenantId);
}
