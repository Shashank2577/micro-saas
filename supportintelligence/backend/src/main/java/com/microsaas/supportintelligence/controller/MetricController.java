package com.microsaas.supportintelligence.controller;

import com.microsaas.supportintelligence.model.AgentMetric;
import com.microsaas.supportintelligence.service.AgentProductivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Tag(name = "Metrics", description = "Agent metrics API")
public class MetricController {

    private final AgentProductivityService metricService;

    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    @Operation(summary = "Get all agent metrics")
    @GetMapping
    public ResponseEntity<List<AgentMetric>> getAllMetrics() {
        return ResponseEntity.ok(metricService.getAllMetrics(getTenantId()));
    }

    @Operation(summary = "Get metrics for specific agent")
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<AgentMetric>> getMetricsForAgent(@PathVariable UUID agentId) {
        return ResponseEntity.ok(metricService.getMetricsForAgent(getTenantId(), agentId));
    }

    @Operation(summary = "Calculate metrics for agent")
    @PostMapping("/agent/{agentId}/calculate")
    public ResponseEntity<Void> calculateMetrics(@PathVariable UUID agentId) {
        metricService.calculateMetrics(getTenantId(), agentId);
        return ResponseEntity.ok().build();
    }
}
