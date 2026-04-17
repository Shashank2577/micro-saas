package com.microsaas.datalineagetracker.controller;

import com.microsaas.datalineagetracker.dto.AuditEvaluationRequest;
import com.microsaas.datalineagetracker.entity.AuditLog;
import com.microsaas.datalineagetracker.entity.Incident;
import com.microsaas.datalineagetracker.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Audit & Security", description = "Endpoints for audit trails and incidents")
public class AuditController {
    private final AuditService service;

    @GetMapping("/audit-logs")
    @Operation(summary = "List all audit logs")
    public List<AuditLog> getAuditLogs() {
        return service.getAllAuditLogs();
    }

    @PostMapping("/audit-logs/evaluate")
    @Operation(summary = "Evaluate an access request and record audit log")
    public AuditLog evaluateAccess(@RequestBody AuditEvaluationRequest request) {
        return service.evaluateAccess(request);
    }
    
    @GetMapping("/incidents")
    @Operation(summary = "List all incidents")
    public List<Incident> getIncidents() {
        return service.getAllIncidents();
    }
}
