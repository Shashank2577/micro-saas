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

    @PostMapping("/mappings/{id}/approve")
    public EvidenceMapping approveMapping(@PathVariable UUID id) {
        return auditVaultService.approveMapping(id);
    }

    @PostMapping("/mappings/{id}/reject")
    public EvidenceMapping rejectMapping(@PathVariable UUID id) {
        return auditVaultService.rejectMapping(id);
    }
}
