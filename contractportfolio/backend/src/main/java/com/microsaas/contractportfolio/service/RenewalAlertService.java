package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.RenewalAlert;
import com.microsaas.contractportfolio.repository.RenewalAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RenewalAlertService {
    private final RenewalAlertRepository repository;

    public RenewalAlert create(RenewalAlert entity) {
        return repository.save(entity);
    }

    public Optional<RenewalAlert> update(UUID id, UUID tenantId, RenewalAlert updateDetails) {
        return repository.findByIdAndTenantId(id, tenantId).map(existing -> {
            existing.setName(updateDetails.getName());
            existing.setStatus(updateDetails.getStatus());
            existing.setMetadataJson(updateDetails.getMetadataJson());
            return repository.save(existing);
        });
    }

    public List<RenewalAlert> list(UUID tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    public Optional<RenewalAlert> getById(UUID id, UUID tenantId) {
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
