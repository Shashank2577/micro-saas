package com.microsaas.agentops.service;

import com.microsaas.agentops.model.AgentHealthMetric;
import com.microsaas.agentops.model.CostAllocation;
import com.microsaas.agentops.repository.AgentHealthMetricRepository;
import com.microsaas.agentops.repository.CostAllocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final AgentHealthMetricRepository healthMetricRepository;
    private final CostAllocationRepository costAllocationRepository;

    @Transactional(readOnly = true)
    public List<AgentHealthMetric> getHealthMetrics(UUID tenantId) {
        return healthMetricRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public List<CostAllocation> getCostAllocations(UUID tenantId) {
        return costAllocationRepository.findByTenantId(tenantId);
    }
}
