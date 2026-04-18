package com.microsaas.apievolver.service;

import com.microsaas.apievolver.model.BreakingChange;
import com.microsaas.apievolver.repository.BreakingChangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BreakingChangeService {
    private final BreakingChangeRepository repository;
    private final EventPublisher eventPublisher;

    public List<BreakingChange> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public BreakingChange findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("BreakingChange not found"));
    }

    @Transactional
    public BreakingChange create(BreakingChange entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        BreakingChange saved = repository.save(entity);
        eventPublisher.publish("apievolver.breaking-change.detected", tenantId, saved.getId().toString());
        return saved;
    }

    @Transactional
    public BreakingChange update(UUID id, BreakingChange entity, UUID tenantId) {
        BreakingChange existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }
}
