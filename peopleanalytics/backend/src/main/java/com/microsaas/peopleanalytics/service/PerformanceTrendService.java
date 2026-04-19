package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.dto.ValidateResponse;
import com.microsaas.peopleanalytics.model.PerformanceTrend;
import com.microsaas.peopleanalytics.repository.PerformanceTrendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerformanceTrendService {
    private final PerformanceTrendRepository repository;

    @Transactional(readOnly = true)
    public List<PerformanceTrend> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public PerformanceTrend findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("PerformanceTrend not found"));
    }

    @Transactional
    public PerformanceTrend create(PerformanceTrend entity, UUID tenantId) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    @Transactional
    public PerformanceTrend update(UUID id, PerformanceTrend entity, UUID tenantId) {
        PerformanceTrend existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public ValidateResponse validate(UUID id, UUID tenantId) {
        PerformanceTrend existing = findById(id, tenantId);
        return new ValidateResponse(true, "PerformanceTrend validated successfully");
    }
}
