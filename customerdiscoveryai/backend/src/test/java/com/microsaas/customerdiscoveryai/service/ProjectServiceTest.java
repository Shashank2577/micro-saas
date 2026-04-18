package com.microsaas.customerdiscoveryai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.customerdiscoveryai.model.Insight;
import com.microsaas.customerdiscoveryai.model.Interview;
import com.microsaas.customerdiscoveryai.model.Report;
import com.microsaas.customerdiscoveryai.model.ResearchProject;
import com.microsaas.customerdiscoveryai.repository.InsightRepository;
import com.microsaas.customerdiscoveryai.repository.InterviewRepository;
import com.microsaas.customerdiscoveryai.repository.ReportRepository;
import com.microsaas.customerdiscoveryai.repository.ResearchProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ResearchProjectRepository projectRepo;
    @Mock
    private InterviewRepository interviewRepo;
    @Mock
    private InsightRepository insightRepo;
    @Mock
    private ReportRepository reportRepo;
    @Mock
    private AiSynthesisService aiSynthesisService;

    @InjectMocks
    private ProjectService projectService;

    private UUID tenantId;
    private UUID projectId;
    private UUID interviewId;
    private ResearchProject project;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        projectId = UUID.randomUUID();
        interviewId = UUID.randomUUID();
        TenantContext.set(tenantId);

        project = new ResearchProject();
        project.setId(projectId);
        project.setTenantId(tenantId);
        project.setName("Test Project");
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void createProject_setsTenantIdAndSaves() {
        when(projectRepo.save(any(ResearchProject.class))).thenReturn(project);

        ResearchProject result = projectService.createProject(new ResearchProject());

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(projectRepo).save(any(ResearchProject.class));
    }

    @Test
    void submitTranscript_updatesInterview() {
        Interview interview = new Interview();
        interview.setId(interviewId);
        interview.setTenantId(tenantId);
        interview.setStatus("PENDING");

        when(interviewRepo.findByIdAndTenantId(interviewId, tenantId)).thenReturn(Optional.of(interview));
        when(interviewRepo.save(any(Interview.class))).thenReturn(interview);

        Interview result = projectService.submitTranscript(interviewId, "Some transcript");

        assertEquals("COMPLETED", result.getStatus());
        assertEquals("Some transcript", result.getTranscript());
        verify(interviewRepo).save(interview);
    }

    @Test
    void synthesizeInsights_processesCompletedInterviews() {
        Interview completedInterview = new Interview();
        completedInterview.setStatus("COMPLETED");
        completedInterview.setTranscript("test");

        Insight mockInsight = new Insight();
        mockInsight.setTheme("Test Theme");

        when(projectRepo.findByIdAndTenantId(projectId, tenantId)).thenReturn(Optional.of(project));
        when(interviewRepo.findByProjectIdAndTenantId(projectId, tenantId)).thenReturn(List.of(completedInterview));
        when(insightRepo.findByProjectIdAndTenantId(projectId, tenantId)).thenReturn(List.of());
        when(aiSynthesisService.synthesizeInsights(anyList(), eq(projectId), eq(tenantId))).thenReturn(List.of(mockInsight));
        when(insightRepo.saveAll(anyIterable())).thenReturn(List.of(mockInsight));

        List<Insight> results = projectService.synthesizeInsights(projectId);

        assertFalse(results.isEmpty());
        assertEquals("Test Theme", results.get(0).getTheme());
        verify(aiSynthesisService).synthesizeInsights(anyList(), eq(projectId), eq(tenantId));
        verify(insightRepo).saveAll(anyIterable());
    }

    @Test
    void generateReport_createsReportFromInsights() {
        Insight insight = new Insight();
        insight.setTheme("Theme 1");

        when(projectRepo.findByIdAndTenantId(projectId, tenantId)).thenReturn(Optional.of(project));
        when(insightRepo.findByProjectIdAndTenantId(projectId, tenantId)).thenReturn(List.of(insight));
        when(aiSynthesisService.generateReport(anyList(), any(), any())).thenReturn("# Report Content");
        when(reportRepo.save(any(Report.class))).thenAnswer(i -> i.getArguments()[0]);

        Report result = projectService.generateReport(projectId);

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        assertEquals("# Report Content", result.getContent());
        assertEquals("Research Report: Test Project", result.getTitle());
    }
}
