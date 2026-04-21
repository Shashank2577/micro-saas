package com.microsaas.constructioniq.project;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.tenancy.TenantContext;
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
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SafetyIncidentRepository incidentRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private ProjectService projectService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void createProject_SetsTenantAndTimestamps() {
        Project project = new Project();
        project.setName("Test Project");

        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArguments()[0]);

        Project created = projectService.createProject(project);

        assertEquals(tenantId, created.getTenantId());
        assertNotNull(created.getCreatedAt());
        assertNotNull(created.getUpdatedAt());
        verify(projectRepository).save(project);
    }

    @Test
    void getProjects_ReturnsOnlyTenantProjects() {
        when(projectRepository.findByTenantId(tenantId)).thenReturn(List.of(new Project()));

        List<Project> projects = projectService.getProjects();

        assertEquals(1, projects.size());
        verify(projectRepository).findByTenantId(tenantId);
    }

    @Test
    void getRiskAssessment_CallsAiService() {
        UUID projectId = UUID.randomUUID();
        Project project = Project.builder().id(projectId).name("Risk Project").build();

        when(projectRepository.findByIdAndTenantId(projectId, tenantId)).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectIdAndTenantId(projectId, tenantId)).thenReturn(List.of());
        when(incidentRepository.findByProjectIdAndTenantId(projectId, tenantId)).thenReturn(List.of());
        when(aiService.chat(any(ChatRequest.class))).thenReturn(new ChatResponse("id", "model", "Risk Assessment Result", new ChatResponse.Usage(1,1,2)));

        String assessment = projectService.getRiskAssessment(projectId);

        assertEquals("Risk Assessment Result", assessment);
        verify(aiService).chat(any(ChatRequest.class));
    }
}
