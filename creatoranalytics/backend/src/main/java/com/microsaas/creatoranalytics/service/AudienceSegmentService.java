package com.microsaas.creatoranalytics.service;

import com.microsaas.creatoranalytics.model.AudienceSegment;
import com.microsaas.creatoranalytics.repository.AudienceSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AudienceSegmentService {
    private final AudienceSegmentRepository repository;

    @Transactional(readOnly = true)
    public List<AudienceSegment> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public AudienceSegment findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("AudienceSegment not found"));
    }

    @Transactional
    public AudienceSegment create(AudienceSegment entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @Transactional
    public AudienceSegment update(UUID id, AudienceSegment update, UUID tenantId) {
        AudienceSegment existing = findById(id, tenantId);
        if (update.getName() != null) existing.setName(update.getName());
        if (update.getStatus() != null) existing.setStatus(update.getStatus());
        if (update.getMetadata() != null) existing.setMetadata(update.getMetadata());
        return repository.save(existing);
    }
}
