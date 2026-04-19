package com.microsaas.legalresearch.service;

import com.microsaas.legalresearch.domain.ArgumentGraph;
import com.microsaas.legalresearch.repository.ArgumentGraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArgumentGraphService {
    private final ArgumentGraphRepository repository;

    @Transactional(readOnly = true)
    public List<ArgumentGraph> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public ArgumentGraph findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("ArgumentGraph not found"));
    }

    @Transactional
    public ArgumentGraph create(ArgumentGraph entity) {
        return repository.save(entity);
    }

    @Transactional
    public ArgumentGraph update(UUID id, ArgumentGraph entity, UUID tenantId) {
        ArgumentGraph existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        ArgumentGraph existing = findById(id, tenantId);
        repository.delete(existing);
    }

    public boolean validate(UUID id, UUID tenantId) {
        findById(id, tenantId);
        return true;
    }
}
