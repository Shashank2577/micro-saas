package com.microsaas.apimanager.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.apimanager.model.ApiProject;
import com.microsaas.apimanager.repository.ApiProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ApiProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        TenantContext.set(java.util.UUID.randomUUID());
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testCreateProject() {
        ApiProject project = new ApiProject();
        project.setName("Test API");

        when(projectRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ApiProject saved = projectService.createProject(project);

        assertNotNull(saved.getId());
        assertNotNull(saved.getTenantId());
        assertEquals("Test API", saved.getName());
        verify(projectRepository).save(any());
    }

    @Test
    void testGetAllProjects() {
        ApiProject p = new ApiProject();
        when(projectRepository.findByTenantId(anyString())).thenReturn(List.of(p));

        List<ApiProject> res = projectService.getAllProjects();
        assertEquals(1, res.size());
    }

    @Test
    void testGetProject() {
        UUID id = UUID.randomUUID();
        ApiProject p = new ApiProject();
        when(projectRepository.findByIdAndTenantId(eq(id), anyString())).thenReturn(Optional.of(p));

        Optional<ApiProject> res = projectService.getProject(id);
        assertTrue(res.isPresent());
    }
}
