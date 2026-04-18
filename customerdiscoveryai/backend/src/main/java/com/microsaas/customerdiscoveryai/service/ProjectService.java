package com.microsaas.customerdiscoveryai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.customerdiscoveryai.model.*;
import com.microsaas.customerdiscoveryai.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    private final ResearchProjectRepository projectRepo;
    private final InterviewRepository interviewRepo;
    private final InsightRepository insightRepo;
    private final ReportRepository reportRepo;
    private final AiSynthesisService aiSynthesisService;

    public ProjectService(ResearchProjectRepository projectRepo, InterviewRepository interviewRepo,
                          InsightRepository insightRepo, ReportRepository reportRepo,
                          AiSynthesisService aiSynthesisService) {
        this.projectRepo = projectRepo;
        this.interviewRepo = interviewRepo;
        this.insightRepo = insightRepo;
        this.reportRepo = reportRepo;
        this.aiSynthesisService = aiSynthesisService;
    }

    public List<ResearchProject> listProjects() {
        return projectRepo.findByTenantId(TenantContext.require());
    }

    public ResearchProject createProject(ResearchProject project) {
        project.setTenantId(TenantContext.require());
        return projectRepo.save(project);
    }

    public ResearchProject getProject(UUID id) {
        return projectRepo.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public List<Interview> listInterviews(UUID projectId) {
        return interviewRepo.findByProjectIdAndTenantId(projectId, TenantContext.require());
    }

    public Interview addInterview(UUID projectId, Interview interview) {
        interview.setTenantId(TenantContext.require());
        interview.setProjectId(projectId);
        interview.setStatus("PENDING");
        return interviewRepo.save(interview);
    }

    @Transactional
    public Interview submitTranscript(UUID interviewId, String transcript) {
        Interview interview = interviewRepo.findByIdAndTenantId(interviewId, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        interview.setTranscript(transcript);
        interview.setStatus("COMPLETED");
        return interviewRepo.save(interview);
    }

    @Transactional
    public List<Insight> synthesizeInsights(UUID projectId) {
        UUID tenantId = TenantContext.require();
        ResearchProject project = projectRepo.findByIdAndTenantId(projectId, tenantId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Interview> completedInterviews = interviewRepo.findByProjectIdAndTenantId(projectId, tenantId).stream()
                .filter(i -> "COMPLETED".equals(i.getStatus()))
                .toList();

        if (completedInterviews.isEmpty()) {
            throw new RuntimeException("No completed interviews to synthesize");
        }

        List<Insight> oldInsights = insightRepo.findByProjectIdAndTenantId(projectId, tenantId);
        insightRepo.deleteAll(oldInsights);

        List<Insight> newInsights = aiSynthesisService.synthesizeInsights(completedInterviews, projectId, tenantId);
        return insightRepo.saveAll(newInsights);
    }

    public List<Insight> getInsights(UUID projectId) {
        return insightRepo.findByProjectIdAndTenantId(projectId, TenantContext.require());
    }

    @Transactional
    public Report generateReport(UUID projectId) {
        UUID tenantId = TenantContext.require();
        ResearchProject project = projectRepo.findByIdAndTenantId(projectId, tenantId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Insight> insights = insightRepo.findByProjectIdAndTenantId(projectId, tenantId);

        String markdownContent = aiSynthesisService.generateReport(insights, project.getName(), project.getDescription());

        Report report = new Report();
        report.setTenantId(tenantId);
        report.setProjectId(projectId);
        report.setTitle("Research Report: " + project.getName());
        report.setContent(markdownContent);
        report.setStatus("COMPLETED");

        return reportRepo.save(report);
    }

    public List<Report> listReports(UUID projectId) {
        return reportRepo.findByProjectIdAndTenantId(projectId, TenantContext.require());
    }

    public Report getReport(UUID reportId) {
        return reportRepo.findByIdAndTenantId(reportId, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }
}
