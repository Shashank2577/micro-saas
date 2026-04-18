package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.PaymentPlan;
import com.microsaas.debtnavigator.repository.PaymentPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlansService {
    private final PaymentPlanRepository repository;

    public PaymentPlan create(PaymentPlan plan) {
        return repository.save(plan);
    }

    public List<PaymentPlan> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<PaymentPlan> getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public PaymentPlan update(PaymentPlan plan) {
        return repository.save(plan);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public boolean validate(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId).isPresent();
    }

    public Object simulate(UUID id, UUID tenantId) {
        return "Simulation result for plan " + id;
    }
}
