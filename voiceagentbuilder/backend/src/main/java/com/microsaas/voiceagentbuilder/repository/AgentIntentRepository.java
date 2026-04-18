package com.microsaas.voiceagentbuilder.repository;

import com.microsaas.voiceagentbuilder.model.AgentIntent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentIntentRepository extends JpaRepository<AgentIntent, UUID> {
    List<AgentIntent> findByAgentIdAndTenantId(UUID agentId, UUID tenantId);
    Optional<AgentIntent> findByIdAndAgentIdAndTenantId(UUID id, UUID agentId, UUID tenantId);
}
