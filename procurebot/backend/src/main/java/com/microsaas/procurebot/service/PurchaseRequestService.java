package com.microsaas.procurebot.service;

import com.microsaas.procurebot.dto.PurchaseRequestRequest;
import com.microsaas.procurebot.model.PurchaseRequest;
import com.microsaas.procurebot.repository.PurchaseRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseRequestService {

    private final PurchaseRequestRepository repository;

    @Transactional(readOnly = true)
    public List<PurchaseRequest> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public PurchaseRequest findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("PurchaseRequest not found"));
    }

    @Transactional
    public PurchaseRequest create(UUID tenantId, PurchaseRequestRequest request) {
        PurchaseRequest entity = PurchaseRequest.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public PurchaseRequest update(UUID id, UUID tenantId, PurchaseRequestRequest request) {
        PurchaseRequest entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        PurchaseRequest entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
