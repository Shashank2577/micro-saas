package com.microsaas.agentops.controller;

import com.microsaas.agentops.model.AgentHealthMetric;
import com.microsaas.agentops.model.CostAllocation;
import com.microsaas.agentops.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;

    @GetMapping("/health")
    public ResponseEntity<List<AgentHealthMetric>> getHealthMetrics(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(metricsService.getHealthMetrics(tenantId));
    }

    @GetMapping("/costs")
    public ResponseEntity<List<CostAllocation>> getCostAllocations(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(metricsService.getCostAllocations(tenantId));
    }
}
