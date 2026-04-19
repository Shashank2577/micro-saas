package com.microsaas.auditready.controller;

import com.microsaas.auditready.model.*;
import com.microsaas.auditready.service.AuditReadyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuditReadyController {
    private final AuditReadyService service;

    @GetMapping("/frameworks")
    public List<ControlFramework> getFrameworks() {
        return service.getFrameworks();
    }
    @GetMapping("/frameworks/{id}")
    public ControlFramework getFramework(@PathVariable UUID id) {
        return service.getFramework(id);
    }
    @PostMapping("/frameworks")
    public ControlFramework createFramework(@RequestBody ControlFramework framework) {
        return service.createFramework(framework);
    }

    @GetMapping("/controls")
    public List<Control> getControls() {
        return service.getControls();
    }
    @PostMapping("/controls")
    public Control createControl(@RequestBody Control control) {
        return service.createControl(control);
    }
    @PutMapping("/controls/{id}")
    public Control updateControl(@PathVariable UUID id, @RequestBody Control control) {
        return service.updateControl(id, control);
    }

    @GetMapping("/evidence")
    public List<Evidence> getEvidences() {
        return service.getEvidences();
    }
    @GetMapping("/evidence/{id}")
    public Evidence getEvidence(@PathVariable UUID id) {
        return service.getEvidence(id);
    }
    @PostMapping("/evidence")
    public Evidence createEvidence(@RequestBody Evidence evidence) {
        return service.createEvidence(evidence);
    }

    @GetMapping("/gaps")
    public List<ComplianceGap> getGaps() {
        return service.getGaps();
    }
    @PostMapping("/gaps")
    public ComplianceGap createGap(@RequestBody ComplianceGap gap) {
        return service.createGap(gap);
    }
    @PutMapping("/gaps/{id}")
    public ComplianceGap updateGap(@PathVariable UUID id, @RequestBody ComplianceGap gap) {
        return service.updateGap(id, gap);
    }

    @GetMapping("/reports")
    public List<AuditReport> getReports() {
        return service.getReports();
    }
    @PostMapping("/reports")
    public AuditReport createReport(@RequestBody AuditReport report) {
        return service.createReport(report);
    }

    @GetMapping("/remediations")
    public List<RemediationWorkflow> getRemediations() {
        return service.getRemediations();
    }
    @PostMapping("/remediations")
    public RemediationWorkflow createRemediation(@RequestBody RemediationWorkflow remediation) {
        return service.createRemediation(remediation);
    }
    @PutMapping("/remediations/{id}")
    public RemediationWorkflow updateRemediation(@PathVariable UUID id, @RequestBody RemediationWorkflow remediation) {
        return service.updateRemediation(id, remediation);
    }

    @GetMapping("/audit-trails")
    public List<AuditTrail> getAuditTrails() {
        return service.getAuditTrails();
    }
}
