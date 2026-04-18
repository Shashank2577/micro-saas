package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.dto.ValidateResponse;
import com.microsaas.peopleanalytics.model.HeadcountMetric;
import com.microsaas.peopleanalytics.repository.HeadcountMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeadcountMetricService {
    private final HeadcountMetricRepository repository;

    @Transactional(readOnly = true)
    public List<HeadcountMetric> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public HeadcountMetric findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("HeadcountMetric not found"));
    }

    @Transactional
    public HeadcountMetric create(HeadcountMetric entity, UUID tenantId) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    @Transactional
    public HeadcountMetric update(UUID id, HeadcountMetric entity, UUID tenantId) {
        HeadcountMetric existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public ValidateResponse validate(UUID id, UUID tenantId) {
        HeadcountMetric existing = findById(id, tenantId);
        return new ValidateResponse(true, "HeadcountMetric validated successfully");
    }
}
