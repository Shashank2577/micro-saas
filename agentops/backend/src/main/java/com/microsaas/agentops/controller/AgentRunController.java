package com.microsaas.agentops.controller;

import com.microsaas.agentops.dto.AgentRunRequest;
import com.microsaas.agentops.dto.AgentStepRequest;
import com.microsaas.agentops.dto.EscalationRequest;
import com.microsaas.agentops.model.AgentRun;
import com.microsaas.agentops.model.AgentStep;
import com.microsaas.agentops.model.HumanEscalation;
import com.microsaas.agentops.service.AgentRunService;
import com.microsaas.agentops.service.AgentStepService;
import com.microsaas.agentops.service.HumanEscalationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/runs")
@RequiredArgsConstructor
public class AgentRunController {

    private final AgentRunService runService;
    private final AgentStepService stepService;
    private final HumanEscalationService escalationService;

    @PostMapping
    public ResponseEntity<AgentRun> startRun(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody AgentRunRequest request) {
        return ResponseEntity.ok(runService.startRun(tenantId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentRun> updateRun(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id,
            @RequestBody AgentRunRequest request) {
        return ResponseEntity.ok(runService.updateRun(tenantId, id, request));
    }

    @GetMapping
    public ResponseEntity<List<AgentRun>> getRuns(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(runService.getRuns(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentRun> getRun(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(runService.getRun(tenantId, id));
    }

    @PostMapping("/{id}/steps")
    public ResponseEntity<AgentStep> addStep(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id,
            @RequestBody AgentStepRequest request) {
        return ResponseEntity.ok(stepService.addStep(tenantId, id, request));
    }

    @GetMapping("/{id}/steps")
    public ResponseEntity<List<AgentStep>> getSteps(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(stepService.getStepsForRun(tenantId, id));
    }

    @PostMapping("/{id}/escalate")
    public ResponseEntity<HumanEscalation> escalateRun(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id,
            @RequestBody EscalationRequest request) {
        return ResponseEntity.ok(escalationService.escalate(tenantId, id, request));
    }
}
