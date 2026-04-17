package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.entity.AuditResult;
import com.microsaas.brandvoice.repository.AuditResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/integration")
@RequiredArgsConstructor
public class IntegrationController {

    private final AuditResultRepository auditResultRepository;

    @GetMapping("/consistency-scores")
    public ResponseEntity<List<AuditResult>> getConsistencyScores(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {

        // This endpoint provides consistency scores for cross-app API usage
        List<AuditResult> scores = auditResultRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        return ResponseEntity.ok(scores);
    }
}
