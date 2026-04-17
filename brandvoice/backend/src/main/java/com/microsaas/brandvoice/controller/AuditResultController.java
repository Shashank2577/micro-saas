package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.entity.AuditFinding;
import com.microsaas.brandvoice.entity.AuditResult;
import com.microsaas.brandvoice.repository.AuditFindingRepository;
import com.microsaas.brandvoice.repository.AuditResultRepository;
import com.microsaas.brandvoice.service.BrandVoiceAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/content-audits")
@RequiredArgsConstructor
public class AuditResultController {

    private final AuditResultRepository resultRepository;
    private final AuditFindingRepository findingRepository;
    private final BrandVoiceAnalysisService analysisService;

    @GetMapping("/{auditId}/results")
    public ResponseEntity<AuditResult> getAuditResult(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID auditId) {
        AuditResult result = resultRepository.findByContentAuditIdAndTenantId(auditId, tenantId)
                .orElseThrow(() -> new RuntimeException("Result not found"));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{auditId}/results/findings")
    public ResponseEntity<List<AuditFinding>> getAuditFindings(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID auditId) {
        AuditResult result = resultRepository.findByContentAuditIdAndTenantId(auditId, tenantId)
                .orElseThrow(() -> new RuntimeException("Result not found"));

        List<AuditFinding> findings = findingRepository.findByAuditResultIdAndTenantId(result.getId(), tenantId);
        return ResponseEntity.ok(findings);
    }

    @PostMapping("/{auditId}/process")
    public ResponseEntity<Void> processAudit(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID auditId) {
        analysisService.processAudit(tenantId, auditId);
        return ResponseEntity.ok().build();
    }
}
