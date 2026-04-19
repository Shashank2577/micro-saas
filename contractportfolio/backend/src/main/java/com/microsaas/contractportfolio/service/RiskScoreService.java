package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.RiskScore;
import com.microsaas.contractportfolio.repository.RiskScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiskScoreService {
    private final RiskScoreRepository repository;

    public RiskScore create(RiskScore entity) {
        return repository.save(entity);
    }

    public Optional<RiskScore> update(UUID id, UUID tenantId, RiskScore updateDetails) {
        return repository.findByIdAndTenantId(id, tenantId).map(existing -> {
            existing.setName(updateDetails.getName());
            existing.setStatus(updateDetails.getStatus());
            existing.setMetadataJson(updateDetails.getMetadataJson());
            return repository.save(existing);
        });
    }

    public List<RiskScore> list(UUID tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    public Optional<RiskScore> getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public void delete(UUID id, UUID tenantId) {
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }

    public boolean validate(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId).isPresent();
    }

    public String simulate(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(e -> "Simulation success for " + e.getName())
                .orElse("Entity not found");
    }
}
