package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.ClauseExtraction;
import com.microsaas.contractportfolio.repository.ClauseExtractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClauseExtractionService {
    private final ClauseExtractionRepository repository;

    public ClauseExtraction create(ClauseExtraction entity) {
        return repository.save(entity);
    }

    public Optional<ClauseExtraction> update(UUID id, UUID tenantId, ClauseExtraction updateDetails) {
        return repository.findByIdAndTenantId(id, tenantId).map(existing -> {
            existing.setName(updateDetails.getName());
            existing.setStatus(updateDetails.getStatus());
            existing.setMetadataJson(updateDetails.getMetadataJson());
            return repository.save(existing);
        });
    }

    public List<ClauseExtraction> list(UUID tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    public Optional<ClauseExtraction> getById(UUID id, UUID tenantId) {
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
