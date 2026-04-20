package com.microsaas.supportintelligence.controller;

import com.microsaas.supportintelligence.model.EscalationSignal;
import com.microsaas.supportintelligence.service.EscalationDetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/escalations")
@RequiredArgsConstructor
@Tag(name = "Escalations", description = "Escalation signals API")
public class EscalationController {

    private final EscalationDetectionService escalationService;

    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    @Operation(summary = "Get all escalations")
    @GetMapping
    public ResponseEntity<List<EscalationSignal>> getAllEscalations() {
        return ResponseEntity.ok(escalationService.getAllEscalations(getTenantId()));
    }

    @Operation(summary = "Resolve an escalation")
    @PostMapping("/{id}/resolve")
    public ResponseEntity<EscalationSignal> resolveEscalation(@PathVariable UUID id) {
        return ResponseEntity.ok(escalationService.resolveEscalation(getTenantId(), id));
    }
}
