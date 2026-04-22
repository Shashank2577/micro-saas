package com.microsaas.licenseguard.service;

import com.microsaas.licenseguard.domain.*;
import com.microsaas.licenseguard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenseGuardService {

    private final RepositoryRepository repositoryRepository;
    private final DependencyRepository dependencyRepository;
    private final LicenseViolationRepository violationRepository;
    private final SbomReportRepository sbomReportRepository;
    private final LicenseRepository licenseRepository;
    private final LicenseObligationRepository obligationRepository;
    private final ScanJobRepository scanJobRepository;
    private final CompliancePolicyRepository policyRepository;

    public List<Repository> getRepositories(UUID tenantId) {
        return repositoryRepository.findByTenantId(tenantId);
    }

    public List<Dependency> getDependencies(UUID tenantId, UUID repositoryId) {
        return dependencyRepository.findByRepositoryIdAndTenantId(repositoryId, tenantId);
    }

    public List<LicenseViolation> getViolations(UUID tenantId, UUID repositoryId) {
        return violationRepository.findByRepositoryIdAndTenantId(repositoryId, tenantId);
    }

    public List<SbomReport> getReports(UUID tenantId, UUID repositoryId) {
        return sbomReportRepository.findByRepositoryIdAndTenantId(repositoryId, tenantId);
    }

    public List<License> getLicenses(UUID tenantId) {
        return licenseRepository.findByTenantId(tenantId);
    }

    public List<LicenseObligation> getObligations(UUID tenantId, UUID licenseId) {
        return obligationRepository.findByLicenseIdAndTenantId(licenseId, tenantId);
    }

    public List<ScanJob> getScanJobs(UUID tenantId, UUID repositoryId) {
        return scanJobRepository.findByRepositoryIdAndTenantId(repositoryId, tenantId);
    }

    public List<ScanJob> getAllScanJobs(UUID tenantId) {
        return scanJobRepository.findByTenantId(tenantId);
    }

    public List<CompliancePolicy> getPolicies(UUID tenantId) {
        return policyRepository.findByTenantId(tenantId);
    }
}
