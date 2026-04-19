package com.microsaas.legalresearch.service;

import com.microsaas.legalresearch.domain.MemoDraft;
import com.microsaas.legalresearch.repository.MemoDraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemoDraftService {
    private final MemoDraftRepository repository;

    @Transactional(readOnly = true)
    public List<MemoDraft> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public MemoDraft findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("MemoDraft not found"));
    }

    @Transactional
    public MemoDraft create(MemoDraft entity) {
        return repository.save(entity);
    }

    @Transactional
    public MemoDraft update(UUID id, MemoDraft entity, UUID tenantId) {
        MemoDraft existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        MemoDraft existing = findById(id, tenantId);
        repository.delete(existing);
    }

    public boolean validate(UUID id, UUID tenantId) {
        findById(id, tenantId);
        return true;
    }
}
