package com.microsaas.auditvault.controller;

import com.microsaas.auditvault.dto.EvidenceIngestRequest;
import com.microsaas.auditvault.dto.PackageGenerateRequest;
import com.microsaas.auditvault.model.*;
import com.microsaas.auditvault.service.AuditVaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuditVaultController {

    private final AuditVaultService auditVaultService;

    @GetMapping("/frameworks")
    public List<Framework> listFrameworks() {
        return auditVaultService.listFrameworks();
    }

    @GetMapping("/frameworks/{id}/controls")
    public List<Control> listControls(@PathVariable UUID id) {
        return auditVaultService.listControls(id);
    }

    @PostMapping("/evidence")
    public Evidence ingestEvidence(@RequestBody EvidenceIngestRequest request) {
        return auditVaultService.ingestEvidence(request);
    }

    @GetMapping("/evidence")
    public List<Evidence> listEvidence() {
        return auditVaultService.listEvidence();
    }

    @GetMapping("/evidence/{id}")
    public Evidence getEvidence(@PathVariable UUID id) {
        return auditVaultService.getEvidence(id);
    }

    @PostMapping("/evidence/{id}/map")
    public EvidenceMapping mapEvidence(@PathVariable UUID id, @RequestParam UUID frameworkId) {
        return auditVaultService.mapEvidence(id, frameworkId);
    }

    @PostMapping("/mappings/{id}/approve")
    public EvidenceMapping approveMapping(@PathVariable UUID id) {
        return auditVaultService.approveMapping(id);
    }

    @PostMapping("/mappings/{id}/reject")
    public EvidenceMapping rejectMapping(@PathVariable UUID id) {
        return auditVaultService.rejectMapping(id);
    }

    @PostMapping("/packages")
    public AuditPackage generatePackage(@RequestBody PackageGenerateRequest request) {
        return auditVaultService.generatePackage(request);
    }

    @GetMapping("/packages")
    public List<AuditPackage> listPackages() {
        return auditVaultService.listPackages();
    }

    @GetMapping("/packages/{id}")
    public AuditPackage getPackage(@PathVariable UUID id) {
        return auditVaultService.getPackage(id);
    }
}
