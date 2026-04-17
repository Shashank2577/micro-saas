package com.microsaas.agentops.controller;

import com.microsaas.agentops.dto.ResolveEscalationRequest;
import com.microsaas.agentops.model.HumanEscalation;
import com.microsaas.agentops.service.HumanEscalationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/escalations")
@RequiredArgsConstructor
public class HumanEscalationController {

    private final HumanEscalationService escalationService;

    @GetMapping
    public ResponseEntity<List<HumanEscalation>> getPendingEscalations(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(escalationService.getPendingEscalations(tenantId));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<HumanEscalation> resolveEscalation(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id,
            @RequestBody ResolveEscalationRequest request) {
        return ResponseEntity.ok(escalationService.resolveEscalation(tenantId, id, request));
    }
}
