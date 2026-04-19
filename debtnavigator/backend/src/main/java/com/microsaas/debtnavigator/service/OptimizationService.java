package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.OptimizationRun;
import com.microsaas.debtnavigator.repository.OptimizationRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OptimizationService {
    private final OptimizationRunRepository repository;

    public OptimizationRun create(OptimizationRun run) {
        return repository.save(run);
    }

    public List<OptimizationRun> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<OptimizationRun> getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public OptimizationRun update(OptimizationRun run) {
        return repository.save(run);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public boolean validate(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId).isPresent();
    }

    public Object simulate(UUID id, UUID tenantId) {
        return "Simulation result for optimization run " + id;
    }
}
