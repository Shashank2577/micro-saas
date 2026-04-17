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
}
