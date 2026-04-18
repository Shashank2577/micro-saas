package com.microsaas.creatoranalytics.service;

import com.microsaas.creatoranalytics.model.PerformanceInsight;
import com.microsaas.creatoranalytics.repository.PerformanceInsightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerformanceInsightService {
    private final PerformanceInsightRepository repository;

    @Transactional(readOnly = true)
    public List<PerformanceInsight> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public PerformanceInsight findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("PerformanceInsight not found"));
    }

    @Transactional
    public PerformanceInsight create(PerformanceInsight entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @Transactional
    public PerformanceInsight update(UUID id, PerformanceInsight update, UUID tenantId) {
        PerformanceInsight existing = findById(id, tenantId);
        if (update.getName() != null) existing.setName(update.getName());
        if (update.getStatus() != null) existing.setStatus(update.getStatus());
        if (update.getMetadata() != null) existing.setMetadata(update.getMetadata());
        return repository.save(existing);
    }
}
