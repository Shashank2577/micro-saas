package com.microsaas.insightengine.repository;

import com.microsaas.insightengine.entity.CustomDiscoveryRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomDiscoveryRuleRepository extends JpaRepository<CustomDiscoveryRule, UUID> {
    List<CustomDiscoveryRule> findByTenantId(UUID tenantId);
    Optional<CustomDiscoveryRule> findByIdAndTenantId(UUID id, UUID tenantId);
}
