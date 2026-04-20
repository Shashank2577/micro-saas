package com.microsaas.supportintelligence.repository;

import com.microsaas.supportintelligence.model.AgentMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentMetricRepository extends JpaRepository<AgentMetric, UUID> {
    List<AgentMetric> findByTenantId(UUID tenantId);
    Optional<AgentMetric> findByIdAndTenantId(UUID id, UUID tenantId);
    List<AgentMetric> findByAgentIdAndTenantId(UUID agentId, UUID tenantId);
}
