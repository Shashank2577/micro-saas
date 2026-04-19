package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.ExperimentPlan;
import com.microsaas.copyoptimizer.repository.ExperimentPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExperimentPlanService {

    private final ExperimentPlanRepository repository;

    @Transactional
    public ExperimentPlan create(ExperimentPlan entity) {
        return repository.save(entity);
    }

    @Transactional
    public ExperimentPlan update(UUID id, UUID tenantId, ExperimentPlan entity) {
        ExperimentPlan existing = getById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<ExperimentPlan> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public ExperimentPlan getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("ExperimentPlan not found"));
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        ExperimentPlan existing = getById(id, tenantId);
        repository.delete(existing);
    }

    public boolean validate(UUID id, UUID tenantId) {
        getById(id, tenantId);
        return true;
    }

    public void simulate(UUID id, UUID tenantId) {
        // Implementation stub
    }
}
