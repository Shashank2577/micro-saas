package com.microsaas.complianceradar.repository;

import com.microsaas.complianceradar.domain.CompliancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompliancePolicyRepository extends JpaRepository<CompliancePolicy, UUID> {
    List<CompliancePolicy> findByTenantId(UUID tenantId);
}
