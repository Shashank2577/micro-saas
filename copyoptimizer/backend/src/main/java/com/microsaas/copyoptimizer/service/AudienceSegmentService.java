package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.AudienceSegment;
import com.microsaas.copyoptimizer.repository.AudienceSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AudienceSegmentService {

    private final AudienceSegmentRepository repository;

    @Transactional
    public AudienceSegment create(AudienceSegment entity) {
        return repository.save(entity);
    }

    @Transactional
    public AudienceSegment update(UUID id, UUID tenantId, AudienceSegment entity) {
        AudienceSegment existing = getById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<AudienceSegment> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public AudienceSegment getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("AudienceSegment not found"));
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        AudienceSegment existing = getById(id, tenantId);
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
