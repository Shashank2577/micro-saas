package com.microsaas.billingai.service;

import com.microsaas.billingai.model.SubscriptionPlan;
import com.microsaas.billingai.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {
    private final SubscriptionPlanRepository repository;

    public List<SubscriptionPlan> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public SubscriptionPlan findById(UUID tenantId, UUID id) {
        return repository.findById(id)
                .filter(plan -> plan.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    public SubscriptionPlan create(UUID tenantId, SubscriptionPlan plan) {
        plan.setId(UUID.randomUUID());
        plan.setTenantId(tenantId);
        plan.setCreatedAt(OffsetDateTime.now());
        plan.setUpdatedAt(OffsetDateTime.now());
        return repository.save(plan);
    }

    public SubscriptionPlan update(UUID tenantId, UUID id, SubscriptionPlan planUpdate) {
        SubscriptionPlan existing = findById(tenantId, id);
        existing.setName(planUpdate.getName());
        existing.setStatus(planUpdate.getStatus());
        existing.setMetadataJson(planUpdate.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public boolean validate(UUID tenantId, UUID id) {
        findById(tenantId, id);
        // Validation logic
        return true;
    }
}
