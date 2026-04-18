package com.microsaas.agencyos.controller;

import com.microsaas.agencyos.domain.Project;
import com.microsaas.agencyos.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<Project> getAllProjects(@RequestParam(required = false) UUID clientId, @RequestHeader("X-Tenant-ID") String tenantId) {
        return projectService.getAllProjects(tenantId, clientId);
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        return projectService.getProjectById(id, tenantId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@RequestBody Project project, @RequestParam UUID clientId, @RequestHeader("X-Tenant-ID") String tenantId) {
        return projectService.createProject(project, clientId, tenantId);
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable UUID id, @RequestBody Project project, @RequestHeader("X-Tenant-ID") String tenantId) {
        return projectService.updateProject(id, project, tenantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        projectService.deleteProject(id, tenantId);
    }
}
