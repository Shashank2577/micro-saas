package com.microsaas.constructioniq.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @GetMapping
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable UUID id) {
        return projectService.getProject(id);
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable UUID id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }

    @PostMapping("/{id}/ai/risk-assessment")
    public Map<String, String> getRiskAssessment(@PathVariable UUID id) {
        return Map.of("assessment", projectService.getRiskAssessment(id));
    }

    @PostMapping("/{id}/ai/schedule-optimization")
    public Map<String, String> optimizeSchedule(@PathVariable UUID id) {
        return Map.of("optimization", projectService.optimizeSchedule(id));
    }
}
