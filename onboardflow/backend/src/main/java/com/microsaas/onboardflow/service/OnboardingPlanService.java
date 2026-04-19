package com.microsaas.onboardflow.service;
import com.microsaas.onboardflow.model.OnboardingPlan;
import com.microsaas.onboardflow.repository.OnboardingPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class OnboardingPlanService {
    private final OnboardingPlanRepository repository;
    public OnboardingPlanService(OnboardingPlanRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<OnboardingPlan> findAll(UUID tenantId) { return repository.findByTenantId(tenantId); }
    @Transactional(readOnly = true)
    public OnboardingPlan findById(UUID id, UUID tenantId) { return repository.findByIdAndTenantId(id, tenantId).orElseThrow(() -> new RuntimeException("Not found")); }
    public OnboardingPlan create(OnboardingPlan entity, UUID tenantId) { entity.setId(UUID.randomUUID()); entity.setTenantId(tenantId); return repository.save(entity); }
    public OnboardingPlan update(UUID id, OnboardingPlan updateContent, UUID tenantId) {
        OnboardingPlan existing = findById(id, tenantId);
        if (updateContent.getName() != null) existing.setName(updateContent.getName());
        return repository.save(existing);
    }
}
