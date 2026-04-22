package com.microsaas.ghostwriter.service;

import com.microsaas.ghostwriter.model.Document;
import com.microsaas.ghostwriter.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository repository;

    public List<Document> getAll(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<Document> getById(UUID id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public Document create(Document document, String tenantId) {
        document.setTenantId(tenantId);
        return repository.save(document);
    }

    @Transactional
    public Document update(UUID id, Document document, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(existing -> {
                    existing.setTitle(document.getTitle());
                    existing.setContent(document.getContent());
                    existing.setStatus(document.getStatus());
                    existing.setProject(document.getProject());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @Transactional
    public void delete(UUID id, String tenantId) {
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
