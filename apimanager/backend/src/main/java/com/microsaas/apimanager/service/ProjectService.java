package com.microsaas.apimanager.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.apimanager.model.ApiProject;
import com.microsaas.apimanager.repository.ApiProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ApiProjectRepository projectRepository;

    public List<ApiProject> getAllProjects() {
        return projectRepository.findByTenantId(TenantContext.require().toString());
    }

    public Optional<ApiProject> getProject(UUID id) {
        return projectRepository.findByIdAndTenantId(id, TenantContext.require().toString());
    }

    public ApiProject createProject(ApiProject project) {
        project.setId(UUID.randomUUID());
        project.setTenantId(TenantContext.require().toString());
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }
}
