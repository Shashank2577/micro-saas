package com.microsaas.complianceradar.controller;

import com.microsaas.complianceradar.domain.ComplianceGap;
import com.microsaas.complianceradar.domain.ComplianceTask;
import com.microsaas.complianceradar.domain.ImpactLevel;
import com.microsaas.complianceradar.domain.RegulatoryChange;
import com.microsaas.complianceradar.service.ComplianceTaskService;
import com.microsaas.complianceradar.service.GapAnalysisService;
import com.microsaas.complianceradar.service.RegulatoryChangeService;
import com.microsaas.complianceradar.repository.ComplianceGapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ComplianceController {

    private final RegulatoryChangeService regulatoryChangeService;
    private final GapAnalysisService gapAnalysisService;
    private final ComplianceTaskService complianceTaskService;
    private final ComplianceGapRepository complianceGapRepository;

    public record AddRegulatoryChangeRequest(String jurisdiction, String name, String summary, LocalDate effectiveDate, ImpactLevel impact) {}

    @PostMapping("/regulations")
    public ResponseEntity<RegulatoryChange> addChange(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody AddRegulatoryChangeRequest request) {
        return ResponseEntity.ok(regulatoryChangeService.addChange(
                request.jurisdiction(),
                request.name(),
                request.summary(),
                request.effectiveDate(),
                request.impact(),
                tenantId
        ));
    }

    @GetMapping("/regulations")
    public ResponseEntity<List<RegulatoryChange>> listChanges(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(regulatoryChangeService.listChanges(tenantId));
    }

    @PostMapping("/regulations/{id}/analyze-gaps")
    public ResponseEntity<List<ComplianceGap>> analyzeGaps(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(gapAnalysisService.analyzeGaps(id, tenantId));
    }

    @GetMapping("/gaps")
    public ResponseEntity<List<ComplianceGap>> listGaps(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(complianceGapRepository.findByTenantId(tenantId));
    }

    public record CreateTaskRequest(UUID gapId, String description, LocalDate dueDate, String assignedTo) {}

    @PostMapping("/tasks")
    public ResponseEntity<ComplianceTask> createTask(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(complianceTaskService.createTask(
                request.gapId(),
                request.description(),
                request.dueDate(),
                request.assignedTo(),
                tenantId
        ));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<ComplianceTask>> listTasks(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(complianceTaskService.listTasks(tenantId));
    }

    @PatchMapping("/tasks/{id}/complete")
    public ResponseEntity<ComplianceTask> completeTask(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(complianceTaskService.completeTask(id, tenantId));
    }
}
