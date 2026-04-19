package com.microsaas.creatoranalytics.service;

import com.microsaas.creatoranalytics.model.AttributionModel;
import com.microsaas.creatoranalytics.repository.AttributionModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttributionModelService {
    private final AttributionModelRepository repository;

    @Transactional(readOnly = true)
    public List<AttributionModel> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public AttributionModel findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("AttributionModel not found"));
    }

    @Transactional
    public AttributionModel create(AttributionModel entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @Transactional
    public AttributionModel update(UUID id, AttributionModel update, UUID tenantId) {
        AttributionModel existing = findById(id, tenantId);
        if (update.getName() != null) existing.setName(update.getName());
        if (update.getStatus() != null) existing.setStatus(update.getStatus());
        if (update.getMetadata() != null) existing.setMetadata(update.getMetadata());
        return repository.save(existing);
    }
}
