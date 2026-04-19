package com.microsaas.apievolver.service;

import com.microsaas.apievolver.model.CompatibilityReport;
import com.microsaas.apievolver.repository.CompatibilityReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompatibilityReportService {
    private final CompatibilityReportRepository repository;

    public List<CompatibilityReport> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public CompatibilityReport findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("CompatibilityReport not found"));
    }

    @Transactional
    public CompatibilityReport create(CompatibilityReport entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @Transactional
    public CompatibilityReport update(UUID id, CompatibilityReport entity, UUID tenantId) {
        CompatibilityReport existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }
}
