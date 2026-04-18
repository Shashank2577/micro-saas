package com.microsaas.creatoranalytics.service;

import com.microsaas.creatoranalytics.model.ContentAsset;
import com.microsaas.creatoranalytics.repository.ContentAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentAssetService {
    private final ContentAssetRepository repository;

    @Transactional(readOnly = true)
    public List<ContentAsset> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public ContentAsset findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("ContentAsset not found"));
    }

    @Transactional
    public ContentAsset create(ContentAsset entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @Transactional
    public ContentAsset update(UUID id, ContentAsset update, UUID tenantId) {
        ContentAsset existing = findById(id, tenantId);
        if (update.getName() != null) existing.setName(update.getName());
        if (update.getStatus() != null) existing.setStatus(update.getStatus());
        if (update.getMetadata() != null) existing.setMetadata(update.getMetadata());
        return repository.save(existing);
    }
}
