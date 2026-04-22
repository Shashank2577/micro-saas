package com.microsaas.dependencyradar.service;

import com.microsaas.dependencyradar.model.DependencyReportItem;
import com.microsaas.dependencyradar.repository.DependencyReportItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DependencyReportItemService {
    private final DependencyReportItemRepository repository;

    public List<DependencyReportItem> findAll() {
        String tenantId = "system";
        return repository.findByTenantId(tenantId);
    }

    public Optional<DependencyReportItem> findById(UUID id) {
        String tenantId = "system";
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public DependencyReportItem save(DependencyReportItem entity) {
        String tenantId = "system";
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        String tenantId = "system";
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
