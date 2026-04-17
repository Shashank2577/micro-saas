package com.microsaas.agentops.repository;

import com.microsaas.agentops.model.AgentStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentStepRepository extends JpaRepository<AgentStep, UUID> {
    List<AgentStep> findByRunIdAndTenantId(UUID runId, UUID tenantId);
}
