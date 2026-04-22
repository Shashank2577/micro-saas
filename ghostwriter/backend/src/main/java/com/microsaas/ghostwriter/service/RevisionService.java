package com.microsaas.ghostwriter.service;

import com.microsaas.ghostwriter.model.Revision;
import com.microsaas.ghostwriter.repository.RevisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RevisionService {
    private final RevisionRepository repository;

    public List<Revision> getByDocumentId(UUID documentId, String tenantId) {
        return repository.findByDocumentIdAndTenantId(documentId, tenantId);
    }

    public Optional<Revision> getById(UUID id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public Revision create(Revision revision, String tenantId) {
        revision.setTenantId(tenantId);
        return repository.save(revision);
    }

    @Transactional
    public void delete(UUID id, String tenantId) {
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
