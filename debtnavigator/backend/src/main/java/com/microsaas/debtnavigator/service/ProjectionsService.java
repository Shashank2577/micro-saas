package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.RiskProjection;
import com.microsaas.debtnavigator.repository.RiskProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectionsService {
    private final RiskProjectionRepository repository;

    public RiskProjection create(RiskProjection projection) {
        return repository.save(projection);
    }

    public List<RiskProjection> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<RiskProjection> getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public RiskProjection update(RiskProjection projection) {
        return repository.save(projection);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public boolean validate(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId).isPresent();
    }

    public Object simulate(UUID id, UUID tenantId) {
        return "Simulation result for projection " + id;
    }
}
