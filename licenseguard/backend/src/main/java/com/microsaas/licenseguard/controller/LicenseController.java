package com.microsaas.licenseguard.controller;

import com.microsaas.licenseguard.domain.Dependency;
import com.microsaas.licenseguard.domain.LicenseViolation;
import com.microsaas.licenseguard.domain.Repository;
import com.microsaas.licenseguard.domain.SbomReport;
import com.microsaas.licenseguard.repository.DependencyRepository;
import com.microsaas.licenseguard.repository.LicenseViolationRepository;
import com.microsaas.licenseguard.repository.RepositoryRepository;
import com.microsaas.licenseguard.repository.SbomReportRepository;
import com.microsaas.licenseguard.service.SbomService;
import com.microsaas.licenseguard.service.ScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/repositories")
@RequiredArgsConstructor
public class LicenseController {

    private final ScanService scanService;
    private final SbomService sbomService;
    private final RepositoryRepository repositoryRepository;
    private final DependencyRepository dependencyRepository;
    private final LicenseViolationRepository violationRepository;
    private final SbomReportRepository sbomReportRepository;

    @PostMapping
    public ResponseEntity<Repository> registerRepository(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(scanService.registerRepo(request.get("name"), request.get("repoUrl"), tenantId));
    }

    @GetMapping
    public ResponseEntity<List<Repository>> getRepositories(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(repositoryRepository.findByTenantId(tenantId));
    }

    @PostMapping("/{id}/scan")
    public ResponseEntity<Void> scanRepository(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        scanService.scanRepo(id, tenantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/dependencies")
    public ResponseEntity<List<Dependency>> getDependencies(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(dependencyRepository.findByRepositoryIdAndTenantId(id, tenantId));
    }

    @GetMapping("/{id}/violations")
    public ResponseEntity<List<LicenseViolation>> getViolations(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(violationRepository.findByRepositoryIdAndTenantId(id, tenantId));
    }

    @PostMapping("/{id}/sbom")
    public ResponseEntity<SbomReport> generateSbom(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(sbomService.generateSbom(id, tenantId));
    }

    @GetMapping("/{id}/sbom")
    public ResponseEntity<List<SbomReport>> getSboms(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(sbomReportRepository.findByRepositoryIdAndTenantId(id, tenantId));
    }
}
