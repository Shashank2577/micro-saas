package com.microsaas.policyforge.repository;

import com.microsaas.policyforge.domain.PolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyVersionRepository extends JpaRepository<PolicyVersion, UUID> {
    List<PolicyVersion> findByPolicyIdAndTenantId(UUID policyId, UUID tenantId);
}
