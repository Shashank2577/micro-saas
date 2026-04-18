package com.microsaas.apimanager.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.apimanager.model.ApiVersion;
import com.microsaas.apimanager.repository.ApiVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VersionService {
    private final ApiVersionRepository versionRepository;

    public List<ApiVersion> getVersions(UUID projectId) {
        return versionRepository.findByProjectIdAndTenantId(projectId, TenantContext.require().toString());
    }

    public ApiVersion createVersion(UUID projectId, ApiVersion version) {
        version.setId(UUID.randomUUID());
        version.setTenantId(TenantContext.require().toString());
        version.setProjectId(projectId);
        version.setStatus("DRAFT");
        version.setCreatedAt(LocalDateTime.now());
        version.setUpdatedAt(LocalDateTime.now());
        return versionRepository.save(version);
    }

    public Optional<ApiVersion> updateStatus(UUID versionId, String status) {
        return versionRepository.findByIdAndTenantId(versionId, TenantContext.require().toString())
            .map(version -> {
                version.setStatus(status);
                version.setUpdatedAt(LocalDateTime.now());
                return versionRepository.save(version);
            });
    }
}
