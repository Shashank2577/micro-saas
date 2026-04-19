package com.microsaas.auditready.repository;

import com.microsaas.auditready.model.RemediationWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RemediationWorkflowRepository extends JpaRepository<RemediationWorkflow, UUID> {
    List<RemediationWorkflow> findByTenantId(UUID tenantId);
    Optional<RemediationWorkflow> findByIdAndTenantId(UUID id, UUID tenantId);
}
