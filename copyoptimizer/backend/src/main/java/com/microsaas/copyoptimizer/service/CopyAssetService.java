package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.CopyAsset;
import com.microsaas.copyoptimizer.repository.CopyAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CopyAssetService {

    private final CopyAssetRepository repository;

    @Transactional
    public CopyAsset create(CopyAsset entity) {
        return repository.save(entity);
    }

    @Transactional
    public CopyAsset update(UUID id, UUID tenantId, CopyAsset entity) {
        CopyAsset existing = getById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<CopyAsset> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public CopyAsset getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("CopyAsset not found"));
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        CopyAsset existing = getById(id, tenantId);
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
