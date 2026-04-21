package com.microsaas.integrationbridge.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.integrationbridge.model.AuditLog;
import com.microsaas.integrationbridge.service.AuditService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getLogs(@RequestParam UUID integrationId) {
        return ResponseEntity.ok(auditService.getLogs(integrationId, TenantContext.require()));
    }

    @PostMapping
    public ResponseEntity<Void> logAudit(@RequestBody AuditLogRequest req) {
        auditService.log(
                TenantContext.require(),
                req.getIntegrationId(),
                req.getAction(),
                req.getStatus(),
                req.getRecordsProcessed(),
                req.getErrorMessage()
        );
        return ResponseEntity.ok().build();
    }

    @Data
    public static class AuditLogRequest {
        private UUID integrationId;
        private String action;
        private String status;
        private int recordsProcessed;
        private String errorMessage;
    }
}
