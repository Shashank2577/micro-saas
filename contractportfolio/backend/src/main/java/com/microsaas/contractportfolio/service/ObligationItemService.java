package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.ObligationItem;
import com.microsaas.contractportfolio.repository.ObligationItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObligationItemService {
    private final ObligationItemRepository repository;

    public ObligationItem create(ObligationItem entity) {
        return repository.save(entity);
    }

    public Optional<ObligationItem> update(UUID id, UUID tenantId, ObligationItem updateDetails) {
        return repository.findByIdAndTenantId(id, tenantId).map(existing -> {
            existing.setName(updateDetails.getName());
            existing.setStatus(updateDetails.getStatus());
            existing.setMetadataJson(updateDetails.getMetadataJson());
            return repository.save(existing);
        });
    }

    public List<ObligationItem> list(UUID tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    public Optional<ObligationItem> getById(UUID id, UUID tenantId) {
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
