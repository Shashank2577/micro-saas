package com.microsaas.auditvault.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.auditvault.dto.PackageGenerateRequest;
import com.microsaas.auditvault.model.AuditPackage;
import com.microsaas.auditvault.repository.AuditPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditPackageService {
    private final AuditPackageRepository packageRepository;

    @Transactional
    public AuditPackage generatePackage(PackageGenerateRequest request) {
        AuditPackage pkg = AuditPackage.builder()
                .tenantId(TenantContext.require())
                .frameworkId(request.getFrameworkId())
                .name(request.getName())
                .status("READY")
                .generatedAt(OffsetDateTime.now())
                .downloadUrl("/api/v1/packages/dummy-download")
                .build();
        return packageRepository.save(pkg);
    }

    public List<AuditPackage> listPackages() {
        return packageRepository.findByTenantId(TenantContext.require());
    }

    public AuditPackage getPackage(UUID id) {
        AuditPackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));
        if (!pkg.getTenantId().equals(TenantContext.require())) {
            throw new IllegalArgumentException("Tenant mismatch");
        }
        return pkg;
    }
}
