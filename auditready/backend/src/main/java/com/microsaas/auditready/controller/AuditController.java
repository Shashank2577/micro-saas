package com.microsaas.auditready.controller;

import com.microsaas.auditready.domain.AuditFramework;
import com.microsaas.auditready.domain.AuditReadinessScore;
import com.microsaas.auditready.domain.Control;
import com.microsaas.auditready.domain.EvidenceItem;
import com.microsaas.auditready.service.EvidenceService;
import com.microsaas.auditready.service.FrameworkService;
import com.microsaas.auditready.service.ReadinessScoreService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuditController {

    private final FrameworkService frameworkService;
    private final EvidenceService evidenceService;
    private final ReadinessScoreService readinessScoreService;

    @GetMapping("/frameworks")
    public ResponseEntity<List<AuditFramework>> getFrameworks(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(frameworkService.listFrameworks(tenantId));
    }

    @GetMapping("/frameworks/{id}/controls")
    public ResponseEntity<List<Control>> getControls(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(frameworkService.getControls(id, tenantId));
    }

    @PostMapping("/controls/{controlId}/evidence")
    public ResponseEntity<EvidenceItem> addEvidence(
            @PathVariable UUID controlId,
            @RequestBody EvidenceRequest request,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        EvidenceItem item = evidenceService.addEvidence(controlId, request.getSourceType(), request.getContent(), tenantId);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/controls/{controlId}/evidence")
    public ResponseEntity<List<EvidenceItem>> getEvidence(
            @PathVariable UUID controlId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(evidenceService.listEvidence(controlId, tenantId));
    }

    @PatchMapping("/evidence/{id}/accept")
    public ResponseEntity<EvidenceItem> acceptEvidence(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return evidenceService.acceptEvidence(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/evidence/{id}/reject")
    public ResponseEntity<EvidenceItem> rejectEvidence(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return evidenceService.rejectEvidence(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/frameworks/{id}/calculate-score")
    public ResponseEntity<AuditReadinessScore> calculateScore(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        AuditReadinessScore score = readinessScoreService.calculateScore(id, tenantId);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/frameworks/{id}/score")
    public ResponseEntity<AuditReadinessScore> getScore(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return readinessScoreService.getLatestScore(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Data
    public static class EvidenceRequest {
        private EvidenceItem.SourceType sourceType;
        private String content;
    }
}
