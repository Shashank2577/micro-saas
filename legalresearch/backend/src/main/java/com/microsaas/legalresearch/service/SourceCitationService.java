package com.microsaas.legalresearch.service;

import com.microsaas.legalresearch.domain.SourceCitation;
import com.microsaas.legalresearch.repository.SourceCitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SourceCitationService {
    private final SourceCitationRepository repository;

    @Transactional(readOnly = true)
    public List<SourceCitation> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public SourceCitation findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("SourceCitation not found"));
    }

    @Transactional
    public SourceCitation create(SourceCitation entity) {
        return repository.save(entity);
    }

    @Transactional
    public SourceCitation update(UUID id, SourceCitation entity, UUID tenantId) {
        SourceCitation existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        SourceCitation existing = findById(id, tenantId);
        repository.delete(existing);
    }

    public boolean validate(UUID id, UUID tenantId) {
        findById(id, tenantId);
        return true;
    }
}
