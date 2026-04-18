package com.microsaas.apievolver.service;

import com.microsaas.apievolver.model.ApiVersion;
import com.microsaas.apievolver.repository.ApiVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiVersionService {
    private final ApiVersionRepository repository;
    private final EventPublisher eventPublisher;

    public List<ApiVersion> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public ApiVersion findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("ApiVersion not found"));
    }

    @Transactional
    public ApiVersion create(ApiVersion entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        ApiVersion saved = repository.save(entity);
        eventPublisher.publish("apievolver.version.published", tenantId, saved.getId().toString());
        return saved;
    }

    @Transactional
    public ApiVersion update(UUID id, ApiVersion entity, UUID tenantId) {
        ApiVersion existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }
}
