package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.OnboardingFeedbackRequest;
import com.microsaas.onboardflow.model.OnboardingFeedback;
import com.microsaas.onboardflow.repository.OnboardingFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnboardingFeedbackService {

    private final OnboardingFeedbackRepository repository;

    @Transactional(readOnly = true)
    public List<OnboardingFeedback> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public OnboardingFeedback findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("OnboardingFeedback not found"));
    }

    @Transactional
    public OnboardingFeedback create(UUID tenantId, OnboardingFeedbackRequest request) {
        OnboardingFeedback entity = OnboardingFeedback.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public OnboardingFeedback update(UUID id, UUID tenantId, OnboardingFeedbackRequest request) {
        OnboardingFeedback entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        OnboardingFeedback entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
