package com.microsaas.policyforge.repository;

import com.microsaas.policyforge.domain.PolicyAcknowledgment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyAcknowledgmentRepository extends JpaRepository<PolicyAcknowledgment, UUID> {
    List<PolicyAcknowledgment> findByPolicyIdAndTenantId(UUID policyId, UUID tenantId);
    long countByPolicyIdAndTenantId(UUID policyId, UUID tenantId);
}
