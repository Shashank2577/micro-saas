package com.microsaas.licenseguard.controller;

import com.microsaas.licenseguard.domain.*;
import com.microsaas.licenseguard.service.LicenseGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/licenseguard")
@RequiredArgsConstructor
public class LicenseGuardController {

    private final LicenseGuardService service;

    @GetMapping("/repositories")
    public List<Repository> getRepositories(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.getRepositories(tenantId);
    }

    @GetMapping("/repositories/{repositoryId}/dependencies")
    public List<Dependency> getDependencies(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID repositoryId) {
        return service.getDependencies(tenantId, repositoryId);
    }

    @GetMapping("/repositories/{repositoryId}/violations")
    public List<LicenseViolation> getViolations(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID repositoryId) {
        return service.getViolations(tenantId, repositoryId);
    }

    @GetMapping("/repositories/{repositoryId}/reports")
    public List<SbomReport> getReports(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID repositoryId) {
        return service.getReports(tenantId, repositoryId);
    }

    @GetMapping("/licenses")
    public List<License> getLicenses(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.getLicenses(tenantId);
    }

    @GetMapping("/licenses/{licenseId}/obligations")
    public List<LicenseObligation> getObligations(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID licenseId) {
        return service.getObligations(tenantId, licenseId);
    }

    @GetMapping("/repositories/{repositoryId}/scan-jobs")
    public List<ScanJob> getScanJobs(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID repositoryId) {
        return service.getScanJobs(tenantId, repositoryId);
    }

    @GetMapping("/policies")
    public List<CompliancePolicy> getPolicies(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.getPolicies(tenantId);
    }

    @GetMapping("/scan-jobs")
    public List<ScanJob> getAllScanJobs(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        // Simplified for dashboard: Just returning all scan jobs for the tenant
        // In a real app this might use a separate repository method
        return service.getAllScanJobs(tenantId);
    }
}
