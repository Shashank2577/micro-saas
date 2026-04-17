package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.dto.ContentAuditRequest;
import com.microsaas.brandvoice.entity.ContentAudit;
import com.microsaas.brandvoice.service.AuditProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final AuditProcessingService auditProcessingService;

    @PostMapping("/external-content")
    public ResponseEntity<ContentAudit> submitExternalContent(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody ContentAuditRequest request) {

        // This endpoint acts as an external webhook receiver for systems like CMS or Social Media Managers
        ContentAudit audit = auditProcessingService.submitAudit(tenantId, request);
        return ResponseEntity.ok(audit);
    }
}
