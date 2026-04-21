package com.microsaas.securitypulse;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PolicyDecisionRepository extends JpaRepository<PolicyDecision, UUID> {
    List<PolicyDecision> findByTenantId(UUID tenantId);
    PolicyDecision findByScanJobIdAndTenantId(UUID scanJobId, UUID tenantId);
}
