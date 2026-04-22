package com.microsaas.dependencyradar.service;

import com.microsaas.dependencyradar.model.DependencyReport;
import com.microsaas.dependencyradar.repository.DependencyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DependencyReportService {
    private final DependencyReportRepository repository;

    public List<DependencyReport> findAll() {
        String tenantId = "system";
        return repository.findByTenantId(tenantId);
    }

    public Optional<DependencyReport> findById(UUID id) {
        String tenantId = "system";
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public DependencyReport save(DependencyReport entity) {
        String tenantId = "system";
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        String tenantId = "system";
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
