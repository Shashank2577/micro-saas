package com.microsaas.agentops.repository;

import com.microsaas.agentops.model.AgentRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentRunRepository extends JpaRepository<AgentRun, UUID> {
    List<AgentRun> findByTenantId(UUID tenantId);
    Optional<AgentRun> findByIdAndTenantId(UUID id, UUID tenantId);
}
