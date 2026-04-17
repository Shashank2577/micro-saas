package com.microsaas.apigatekeeper.service;

import com.microsaas.apigatekeeper.entity.APIVersion;
import com.microsaas.apigatekeeper.repository.APIVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class APIVersioningService {

    private final APIVersionRepository repository;

    @Transactional
    public APIVersion createVersion(String tenantId, APIVersion version) {
        version.setTenantId(tenantId);
        version.setCreatedAt(ZonedDateTime.now());
        version.setUpdatedAt(ZonedDateTime.now());
        return repository.save(version);
    }

    public List<APIVersion> getVersions(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
