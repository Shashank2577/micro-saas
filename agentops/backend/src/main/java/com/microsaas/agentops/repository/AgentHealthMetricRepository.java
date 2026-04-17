package com.microsaas.agentops.repository;

import com.microsaas.agentops.model.AgentHealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentHealthMetricRepository extends JpaRepository<AgentHealthMetric, UUID> {
    List<AgentHealthMetric> findByTenantId(UUID tenantId);
}
