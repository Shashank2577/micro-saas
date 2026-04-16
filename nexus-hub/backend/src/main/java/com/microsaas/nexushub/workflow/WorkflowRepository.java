package com.microsaas.nexushub.workflow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowRepository extends JpaRepository<Workflow, UUID> {
    List<Workflow> findByTenantIdAndEnabled(UUID tenantId, boolean enabled);
}
