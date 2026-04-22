package com.microsaas.ghostwriter.service;

import com.microsaas.ghostwriter.model.Project;
import com.microsaas.ghostwriter.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository repository;

    public List<Project> getAll(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<Project> getById(UUID id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public Project create(Project project, String tenantId) {
        project.setTenantId(tenantId);
        return repository.save(project);
    }

    @Transactional
    public Project update(UUID id, Project project, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(existing -> {
                    existing.setName(project.getName());
                    existing.setDescription(project.getDescription());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Transactional
    public void delete(UUID id, String tenantId) {
        repository.findByIdAndTenantId(id, tenantId).ifPresent(repository::delete);
    }
}
