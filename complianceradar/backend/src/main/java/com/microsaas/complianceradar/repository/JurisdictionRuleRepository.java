package com.microsaas.complianceradar.repository;

import com.microsaas.complianceradar.domain.JurisdictionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JurisdictionRuleRepository extends JpaRepository<JurisdictionRule, UUID> {
    List<JurisdictionRule> findAllByTenantId(UUID tenantId);
    Optional<JurisdictionRule> findByIdAndTenantId(UUID id, UUID tenantId);
    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}
