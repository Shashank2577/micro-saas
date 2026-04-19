package com.microsaas.legalresearch.service;

import com.microsaas.legalresearch.domain.ResearchQuery;
import com.microsaas.legalresearch.repository.ResearchQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResearchQueryService {
    private final ResearchQueryRepository repository;

    @Transactional(readOnly = true)
    public List<ResearchQuery> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public ResearchQuery findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("ResearchQuery not found"));
    }

    @Transactional
    public ResearchQuery create(ResearchQuery entity) {
        return repository.save(entity);
    }

    @Transactional
    public ResearchQuery update(UUID id, ResearchQuery entity, UUID tenantId) {
        ResearchQuery existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        ResearchQuery existing = findById(id, tenantId);
        repository.delete(existing);
    }

    public boolean validate(UUID id, UUID tenantId) {
        findById(id, tenantId);
        return true;
    }
}
