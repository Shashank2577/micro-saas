package com.microsaas.brandvoice.service;

import com.microsaas.brandvoice.entity.ContentAsset;
import com.microsaas.brandvoice.repository.ContentAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentAssetService {
    private final ContentAssetRepository repository;

    public List<ContentAsset> findAllByTenant(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public ContentAsset save(ContentAsset entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
