package com.microsaas.regulatoryfiling.controller;

import com.microsaas.regulatoryfiling.domain.FilingAlert;
import com.microsaas.regulatoryfiling.domain.FilingDraft;
import com.microsaas.regulatoryfiling.domain.FilingObligation;
import com.microsaas.regulatoryfiling.dto.ObligationRequest;
import com.microsaas.regulatoryfiling.service.AlertService;
import com.microsaas.regulatoryfiling.service.DraftService;
import com.microsaas.regulatoryfiling.service.FilingObligationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FilingController {

    private final FilingObligationService obligationService;
    private final AlertService alertService;
    private final DraftService draftService;

    @PostMapping("/obligations")
    public ResponseEntity<FilingObligation> createObligation(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody ObligationRequest request) {
        FilingObligation obligation = obligationService.addObligation(
                request.getName(),
                request.getJurisdiction(),
                request.getFilingType(),
                request.getDueDate(),
                request.getRecurrencePattern(),
                tenantId
        );
        return ResponseEntity.ok(obligation);
    }

    @GetMapping("/obligations")
    public ResponseEntity<List<FilingObligation>> listObligations(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(obligationService.listObligations(tenantId));
    }

    @GetMapping("/obligations/upcoming")
    public ResponseEntity<List<FilingObligation>> getUpcomingObligations(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(obligationService.getUpcoming(tenantId, days));
    }

    @GetMapping("/obligations/overdue")
    public ResponseEntity<List<FilingObligation>> getOverdueObligations(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(obligationService.getOverdue(tenantId));
    }

    @PostMapping("/obligations/{id}/submit")
    public ResponseEntity<FilingObligation> submitFiling(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(obligationService.submitFiling(id, tenantId));
    }

    @PostMapping("/obligations/{id}/generate-draft")
    public ResponseEntity<FilingDraft> generateDraft(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(draftService.generateDraft(id, tenantId));
    }

    @GetMapping("/obligations/{id}/draft")
    public ResponseEntity<FilingDraft> getDraft(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(draftService.getDraft(id, tenantId));
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<FilingAlert>> listAlerts(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(alertService.listAlerts(tenantId));
    }

    @PostMapping("/alerts/{id}/acknowledge")
    public ResponseEntity<FilingAlert> acknowledgeAlert(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(alertService.acknowledgeAlert(id, tenantId));
    }
}
