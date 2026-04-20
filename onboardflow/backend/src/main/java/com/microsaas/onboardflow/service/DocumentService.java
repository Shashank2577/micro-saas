package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.DocumentRequest;
import com.microsaas.onboardflow.model.Document;
import com.microsaas.onboardflow.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository repository;

    @Transactional(readOnly = true)
    public List<Document> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Document findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @Transactional
    public Document create(UUID tenantId, DocumentRequest request) {
        Document entity = Document.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public Document update(UUID id, UUID tenantId, DocumentRequest request) {
        Document entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        Document entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
