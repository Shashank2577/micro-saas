package com.microsaas.constructioniq.project;

import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project) {
        project.setTenantId(TenantContext.require());
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjects() {
        return projectRepository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Project getProjectById(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getTenantId().equals(TenantContext.require())) {
            throw new RuntimeException("Project not found");
        }
        return project;
    }

    public Project updateProject(UUID id, Project projectDetails) {
        Project project = getProjectById(id);
        project.setName(projectDetails.getName());
        project.setStatus(projectDetails.getStatus());
        project.setBudget(projectDetails.getBudget());
        project.setStartDate(projectDetails.getStartDate());
        project.setEndDate(projectDetails.getEndDate());
        project.setUpdatedAt(OffsetDateTime.now());
        return projectRepository.save(project);
    }

    public void deleteProject(UUID id) {
        Project project = getProjectById(id);
        projectRepository.delete(project);
    }
}
