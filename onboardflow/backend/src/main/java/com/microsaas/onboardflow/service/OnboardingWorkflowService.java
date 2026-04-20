package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.OnboardingWorkflowRequest;
import com.microsaas.onboardflow.model.OnboardingWorkflow;
import com.microsaas.onboardflow.repository.OnboardingWorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnboardingWorkflowService {

    private final OnboardingWorkflowRepository repository;

    @Transactional(readOnly = true)
    public List<OnboardingWorkflow> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public OnboardingWorkflow findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("OnboardingWorkflow not found"));
    }

    @Transactional
    public OnboardingWorkflow create(UUID tenantId, OnboardingWorkflowRequest request) {
        OnboardingWorkflow entity = OnboardingWorkflow.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public OnboardingWorkflow update(UUID id, UUID tenantId, OnboardingWorkflowRequest request) {
        OnboardingWorkflow entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        OnboardingWorkflow entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
