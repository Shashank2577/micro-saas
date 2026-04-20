package com.microsaas.supportintelligence.service;

import com.microsaas.supportintelligence.model.AgentMetric;
import com.microsaas.supportintelligence.repository.AgentMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentProductivityService {

    private final AgentMetricRepository metricRepository;

    @Transactional
    public void calculateMetrics(UUID tenantId, UUID agentId) {
        log.info("Calculating productivity metrics for agent: {}", agentId);
        // Mocking metric calculation logic
        AgentMetric metric = AgentMetric.builder()
                .tenantId(tenantId)
                .agentId(agentId)
                .period("2026-W16")
                .ticketsResolved(45)
                .avgResolutionTime(14.5) // minutes
                .csatScore(4.8)
                .build();
        metricRepository.save(metric);
    }

    @Transactional(readOnly = true)
    public List<AgentMetric> getMetricsForAgent(UUID tenantId, UUID agentId) {
        return metricRepository.findByAgentIdAndTenantId(agentId, tenantId);
    }

    @Transactional(readOnly = true)
    public List<AgentMetric> getAllMetrics(UUID tenantId) {
        return metricRepository.findByTenantId(tenantId);
    }
}
