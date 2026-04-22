package com.microsaas.brandvoice.service;

import com.microsaas.brandvoice.entity.BrandGuideline;
import com.microsaas.brandvoice.repository.BrandGuidelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandGuidelineService {
    private final BrandGuidelineRepository repository;

    public List<BrandGuideline> findAllByTenant(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public BrandGuideline save(BrandGuideline entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
