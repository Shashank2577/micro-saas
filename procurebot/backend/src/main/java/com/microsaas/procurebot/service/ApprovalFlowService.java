package com.microsaas.procurebot.service;

import com.microsaas.procurebot.dto.ApprovalFlowRequest;
import com.microsaas.procurebot.model.ApprovalFlow;
import com.microsaas.procurebot.repository.ApprovalFlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApprovalFlowService {

    private final ApprovalFlowRepository repository;

    @Transactional(readOnly = true)
    public List<ApprovalFlow> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public ApprovalFlow findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("ApprovalFlow not found"));
    }

    @Transactional
    public ApprovalFlow create(UUID tenantId, ApprovalFlowRequest request) {
        ApprovalFlow entity = ApprovalFlow.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public ApprovalFlow update(UUID id, UUID tenantId, ApprovalFlowRequest request) {
        ApprovalFlow entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        ApprovalFlow entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
