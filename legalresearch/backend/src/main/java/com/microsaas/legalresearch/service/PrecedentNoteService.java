package com.microsaas.legalresearch.service;

import com.microsaas.legalresearch.domain.PrecedentNote;
import com.microsaas.legalresearch.repository.PrecedentNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrecedentNoteService {
    private final PrecedentNoteRepository repository;

    @Transactional(readOnly = true)
    public List<PrecedentNote> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public PrecedentNote findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("PrecedentNote not found"));
    }

    @Transactional
    public PrecedentNote create(PrecedentNote entity) {
        return repository.save(entity);
    }

    @Transactional
    public PrecedentNote update(UUID id, PrecedentNote entity, UUID tenantId) {
        PrecedentNote existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        PrecedentNote existing = findById(id, tenantId);
        repository.delete(existing);
    }

    public boolean validate(UUID id, UUID tenantId) {
        findById(id, tenantId);
        return true;
    }
}
