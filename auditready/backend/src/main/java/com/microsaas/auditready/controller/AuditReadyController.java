package com.microsaas.auditready.controller;

import com.microsaas.auditready.model.*;
import com.microsaas.auditready.service.AuditReadyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuditReadyController {
    private final AuditReadyService service;

    @GetMapping("/frameworks")
    public ResponseEntity<List<ControlFramework>> getFrameworks() {
        return ResponseEntity.ok(service.getFrameworks());
    }

    @GetMapping("/frameworks/{id}")
    public ResponseEntity<ControlFramework> getFramework(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getFramework(id));
    }

    @PostMapping("/frameworks")
    public ResponseEntity<ControlFramework> createFramework(@RequestBody ControlFramework framework) {
        return ResponseEntity.ok(service.createFramework(framework));
    }

    @GetMapping("/controls")
    public ResponseEntity<List<Control>> getControls() {
        return ResponseEntity.ok(service.getControls());
    }

    @PostMapping("/controls")
    public ResponseEntity<Control> createControl(@RequestBody Control control) {
        return ResponseEntity.ok(service.createControl(control));
    }

    @PutMapping("/controls/{id}")
    public ResponseEntity<Control> updateControl(@PathVariable UUID id, @RequestBody Control control) {
        return ResponseEntity.ok(service.updateControl(id, control));
    }

    @GetMapping("/gaps")
    public ResponseEntity<List<ComplianceGap>> getGaps() {
        return ResponseEntity.ok(service.getGaps());
    }

    @PostMapping("/gaps")
    public ResponseEntity<ComplianceGap> createGap(@RequestBody ComplianceGap gap) {
        return ResponseEntity.ok(service.createGap(gap));
    }

    @PutMapping("/gaps/{id}")
    public ResponseEntity<ComplianceGap> updateGap(@PathVariable UUID id, @RequestBody ComplianceGap gap) {
        return ResponseEntity.ok(service.updateGap(id, gap));
    }

    @GetMapping("/remediations")
    public ResponseEntity<List<RemediationWorkflow>> getRemediations() {
        return ResponseEntity.ok(service.getRemediations());
    }

    @PostMapping("/remediations")
    public ResponseEntity<RemediationWorkflow> createRemediation(@RequestBody RemediationWorkflow remediation) {
        return ResponseEntity.ok(service.createRemediation(remediation));
    }

    @PutMapping("/remediations/{id}")
    public ResponseEntity<RemediationWorkflow> updateRemediation(@PathVariable UUID id, @RequestBody RemediationWorkflow remediation) {
        return ResponseEntity.ok(service.updateRemediation(id, remediation));
    }

    @GetMapping("/audit-trails")
    public ResponseEntity<List<AuditTrail>> getAuditTrails() {
        return ResponseEntity.ok(service.getAuditTrails());
    }
}
