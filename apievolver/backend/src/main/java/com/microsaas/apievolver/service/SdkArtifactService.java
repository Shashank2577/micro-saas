package com.microsaas.apievolver.service;

import com.microsaas.apievolver.model.SdkArtifact;
import com.microsaas.apievolver.repository.SdkArtifactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SdkArtifactService {
    private final SdkArtifactRepository repository;
    private final EventPublisher eventPublisher;

    public List<SdkArtifact> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public SdkArtifact findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("SdkArtifact not found"));
    }

    @Transactional
    public SdkArtifact create(SdkArtifact entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        SdkArtifact saved = repository.save(entity);
        eventPublisher.publish("apievolver.sdk.generated", tenantId, saved.getId().toString());
        return saved;
    }

    @Transactional
    public SdkArtifact update(UUID id, SdkArtifact entity, UUID tenantId) {
        SdkArtifact existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }
}
