package com.microsaas.constructioniq.project;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private AutoCloseable closeable;
    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() throws Exception {
        TenantContext.clear();
        closeable.close();
    }

    @Test
    void createProject() {
        Project project = new Project();
        project.setName("Test Project");

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.createProject(project);

        assertNotNull(result);
        assertEquals(tenantId, project.getTenantId());
        verify(projectRepository).save(project);
    }

    @Test
    void getProjects() {
        Project project = new Project();
        when(projectRepository.findByTenantId(tenantId)).thenReturn(List.of(project));

        List<Project> results = projectService.getProjects();

        assertFalse(results.isEmpty());
        verify(projectRepository).findByTenantId(tenantId);
    }

    @Test
    void getProjectById_Success() {
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setId(projectId);
        project.setTenantId(tenantId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(projectId);

        assertNotNull(result);
        assertEquals(projectId, result.getId());
    }

    @Test
    void getProjectById_WrongTenant() {
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setId(projectId);
        project.setTenantId(UUID.randomUUID());

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        assertThrows(RuntimeException.class, () -> projectService.getProjectById(projectId));
    }

    @Test
    void updateProject() {
        UUID projectId = UUID.randomUUID();
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setTenantId(tenantId);
        existingProject.setName("Old Name");

        Project updatedDetails = new Project();
        updatedDetails.setName("New Name");
        updatedDetails.setBudget(new BigDecimal("1000.00"));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(existingProject);

        Project result = projectService.updateProject(projectId, updatedDetails);

        assertNotNull(result);
        assertEquals("New Name", existingProject.getName());
        verify(projectRepository).save(existingProject);
    }

    @Test
    void deleteProject() {
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setId(projectId);
        project.setTenantId(tenantId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        projectService.deleteProject(projectId);

        verify(projectRepository).delete(project);
    }
}
