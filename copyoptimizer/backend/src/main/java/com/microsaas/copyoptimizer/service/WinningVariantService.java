package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.WinningVariant;
import com.microsaas.copyoptimizer.repository.WinningVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WinningVariantService {

    private final WinningVariantRepository repository;

    @Transactional
    public WinningVariant create(WinningVariant entity) {
        return repository.save(entity);
    }

    @Transactional
    public WinningVariant update(UUID id, UUID tenantId, WinningVariant entity) {
        WinningVariant existing = getById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<WinningVariant> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public WinningVariant getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("WinningVariant not found"));
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        WinningVariant existing = getById(id, tenantId);
        repository.delete(existing);
    }

    public boolean validate(UUID id, UUID tenantId) {
        getById(id, tenantId);
        return true;
    }

    public void simulate(UUID id, UUID tenantId) {
        // Implementation stub
    }
}
