package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.OnboardingTaskRequest;
import com.microsaas.onboardflow.model.OnboardingTask;
import com.microsaas.onboardflow.repository.OnboardingTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnboardingTaskService {

    private final OnboardingTaskRepository repository;

    @Transactional(readOnly = true)
    public List<OnboardingTask> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public OnboardingTask findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("OnboardingTask not found"));
    }

    @Transactional
    public OnboardingTask create(UUID tenantId, OnboardingTaskRequest request) {
        OnboardingTask entity = OnboardingTask.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public OnboardingTask update(UUID id, UUID tenantId, OnboardingTaskRequest request) {
        OnboardingTask entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        OnboardingTask entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
