package com.microsaas.customerdiscoveryai.controller;

import com.microsaas.customerdiscoveryai.model.*;
import com.microsaas.customerdiscoveryai.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ResearchProject> listProjects() {
        return projectService.listProjects();
    }

    @PostMapping
    public ResearchProject createProject(@RequestBody ResearchProject project) {
        return projectService.createProject(project);
    }

    @GetMapping("/{id}")
    public ResearchProject getProject(@PathVariable UUID id) {
        return projectService.getProject(id);
    }

    @GetMapping("/{projectId}/interviews")
    public List<Interview> listInterviews(@PathVariable UUID projectId) {
        return projectService.listInterviews(projectId);
    }

    @PostMapping("/{projectId}/interviews")
    public Interview addInterview(@PathVariable UUID projectId, @RequestBody Interview interview) {
        return projectService.addInterview(projectId, interview);
    }

    @PostMapping("/{projectId}/synthesize")
    public List<Insight> synthesizeInsights(@PathVariable UUID projectId) {
        return projectService.synthesizeInsights(projectId);
    }

    @GetMapping("/{projectId}/insights")
    public List<Insight> getInsights(@PathVariable UUID projectId) {
        return projectService.getInsights(projectId);
    }

    @PostMapping("/{projectId}/reports")
    public Report generateReport(@PathVariable UUID projectId) {
        return projectService.generateReport(projectId);
    }

    @GetMapping("/{projectId}/reports")
    public List<Report> listReports(@PathVariable UUID projectId) {
        return projectService.listReports(projectId);
    }
}
