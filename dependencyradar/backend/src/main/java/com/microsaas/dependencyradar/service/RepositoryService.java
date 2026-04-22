package com.microsaas.dependencyradar.service;

import com.microsaas.dependencyradar.model.Repository;
import com.microsaas.dependencyradar.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RepositoryService {
    private final RepositoryRepository repository;

    public List<Repository> findAll() {
        String tenantId = "system";
        return repository.findByTenantId(tenantId);
    }

    public Optional<Repository> findById(UUID id) {
        String tenantId = "system";
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public Repository save(Repository entity) {
        String tenantId = "system";
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        String tenantId = "system";
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
