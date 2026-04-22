package com.microsaas.licenseguard.controller;

import com.microsaas.licenseguard.service.LicenseComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/licenseguard/scan")
@RequiredArgsConstructor
public class ScanController {

    private final LicenseComplianceService scanService;

    @PostMapping("/{repositoryId}")
    public ResponseEntity<Void> scanRepository(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID repositoryId) {
        scanService.analyzeRepository(tenantId, repositoryId);
        return ResponseEntity.accepted().build();
    }
}
