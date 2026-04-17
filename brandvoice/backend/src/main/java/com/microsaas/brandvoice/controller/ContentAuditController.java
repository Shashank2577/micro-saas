package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.dto.ContentAuditRequest;
import com.microsaas.brandvoice.entity.ContentAudit;
import com.microsaas.brandvoice.service.AuditProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/content-audits")
@RequiredArgsConstructor
public class ContentAuditController {

    private final AuditProcessingService service;

    @PostMapping
    public ResponseEntity<ContentAudit> submitAudit(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody ContentAuditRequest request) {
        return ResponseEntity.ok(service.submitAudit(tenantId, request));
    }

    @GetMapping
    public ResponseEntity<List<ContentAudit>> getAudits(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.getAudits(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentAudit> getAudit(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.getAudit(tenantId, id));
    }
}
