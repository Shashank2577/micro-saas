package com.microsaas.documentvault.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.documentvault.model.AuditLog;
import com.microsaas.documentvault.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents/{id}/audit")
public class DocumentAuditController {

    private final AuditService auditService;

    public DocumentAuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    private UUID getTenantId() {
        return TenantContext.require();
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAuditLogs(@PathVariable UUID id) {
        return ResponseEntity.ok(auditService.getLogs(id, getTenantId()));
    }
}
