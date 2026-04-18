package com.microsaas.apimanager.service;

import com.microsaas.apimanager.model.ApiVersion;
import com.microsaas.apimanager.repository.ApiVersionRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SdkService {
    private final ApiVersionRepository versionRepository;

    public String generateSdk(UUID versionId, String language) {
        Optional<ApiVersion> versionOpt = versionRepository.findByIdAndTenantId(versionId, TenantContext.require().toString());
        if (versionOpt.isEmpty() || versionOpt.get().getOpenapiSchema() == null) {
            throw new IllegalArgumentException("Version schema not found");
        }
        
        // Simulating SDK generation from schema
        return "Generated " + language + " SDK based on schema:\n" + versionOpt.get().getOpenapiSchema();
    }
}
