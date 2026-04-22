package com.microsaas.dependencyradar.service;

import com.microsaas.dependencyradar.model.ScanJob;
import com.microsaas.dependencyradar.repository.ScanJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScanJobService {
    private final ScanJobRepository repository;

    public List<ScanJob> findAll() {
        String tenantId = "system";
        return repository.findByTenantId(tenantId);
    }

    public Optional<ScanJob> findById(UUID id) {
        String tenantId = "system";
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public ScanJob save(ScanJob entity) {
        String tenantId = "system";
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        String tenantId = "system";
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
