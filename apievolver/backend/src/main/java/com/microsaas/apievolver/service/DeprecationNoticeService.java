package com.microsaas.apievolver.service;

import com.microsaas.apievolver.model.DeprecationNotice;
import com.microsaas.apievolver.repository.DeprecationNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeprecationNoticeService {
    private final DeprecationNoticeRepository repository;
    private final EventPublisher eventPublisher;

    public List<DeprecationNotice> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public DeprecationNotice findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("DeprecationNotice not found"));
    }

    @Transactional
    public DeprecationNotice create(DeprecationNotice entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        DeprecationNotice saved = repository.save(entity);
        eventPublisher.publish("apievolver.api-deprecated", tenantId, saved.getId().toString());
        return saved;
    }

    @Transactional
    public DeprecationNotice update(UUID id, DeprecationNotice entity, UUID tenantId) {
        DeprecationNotice existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }
}
