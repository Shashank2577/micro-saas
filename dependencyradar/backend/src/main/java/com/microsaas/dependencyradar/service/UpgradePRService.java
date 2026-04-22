package com.microsaas.dependencyradar.service;

import com.microsaas.dependencyradar.model.UpgradePR;
import com.microsaas.dependencyradar.repository.UpgradePRRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpgradePRService {
    private final UpgradePRRepository repository;

    public List<UpgradePR> findAll() {
        String tenantId = "system";
        return repository.findByTenantId(tenantId);
    }

    public Optional<UpgradePR> findById(UUID id) {
        String tenantId = "system";
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public UpgradePR save(UpgradePR entity) {
        String tenantId = "system";
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        String tenantId = "system";
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
