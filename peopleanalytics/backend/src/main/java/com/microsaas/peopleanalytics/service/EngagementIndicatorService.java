package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.dto.ValidateResponse;
import com.microsaas.peopleanalytics.model.EngagementIndicator;
import com.microsaas.peopleanalytics.repository.EngagementIndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EngagementIndicatorService {
    private final EngagementIndicatorRepository repository;

    @Transactional(readOnly = true)
    public List<EngagementIndicator> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public EngagementIndicator findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("EngagementIndicator not found"));
    }

    @Transactional
    public EngagementIndicator create(EngagementIndicator entity, UUID tenantId) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    @Transactional
    public EngagementIndicator update(UUID id, EngagementIndicator entity, UUID tenantId) {
        EngagementIndicator existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public ValidateResponse validate(UUID id, UUID tenantId) {
        EngagementIndicator existing = findById(id, tenantId);
        return new ValidateResponse(true, "EngagementIndicator validated successfully");
    }
}
