package com.microsaas.datagovernanceos.controller;

import com.microsaas.datagovernanceos.entity.ComplianceAudit;
import com.microsaas.datagovernanceos.service.GovernanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/compliance-audits")
@RequiredArgsConstructor
public class ComplianceAuditController {

    private final GovernanceService governanceService;

    @GetMapping
    public ResponseEntity<List<ComplianceAudit>> listAudits() {
        return ResponseEntity.ok(governanceService.listAudits());
    }

    @PostMapping("/run")
    public ResponseEntity<Void> runAudits(@RequestParam UUID assetId) {
        governanceService.runAudits(assetId);
        return ResponseEntity.ok().build();
    }
}
