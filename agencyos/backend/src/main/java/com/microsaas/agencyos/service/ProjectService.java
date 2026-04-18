package com.microsaas.agencyos.service;

import com.microsaas.agencyos.domain.Client;
import com.microsaas.agencyos.domain.Project;
import com.microsaas.agencyos.exception.ResourceNotFoundException;
import com.microsaas.agencyos.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ClientService clientService;

    @Transactional(readOnly = true)
    public List<Project> getAllProjects(String tenantId, UUID clientId) {
        if (clientId != null) {
            return projectRepository.findByTenantIdAndClientId(tenantId, clientId);
        }
        return projectRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Project getProjectById(UUID id, String tenantId) {
        return projectRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    @Transactional
    public Project createProject(Project project, UUID clientId, String tenantId) {
        Client client = clientService.getClientById(clientId, tenantId);
        project.setClient(client);
        project.setTenantId(tenantId);
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(UUID id, Project projectDetails, String tenantId) {
        Project project = getProjectById(id, tenantId);
        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
        project.setStatus(projectDetails.getStatus());
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(UUID id, String tenantId) {
        Project project = getProjectById(id, tenantId);
        projectRepository.delete(project);
    }
}
