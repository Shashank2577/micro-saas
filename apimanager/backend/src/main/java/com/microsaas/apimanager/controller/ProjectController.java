package com.microsaas.apimanager.controller;

import com.microsaas.apimanager.model.ApiProject;
import com.microsaas.apimanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ApiProject>> listProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PostMapping
    public ResponseEntity<ApiProject> createProject(@RequestBody ApiProject project) {
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiProject> getProject(@PathVariable UUID id) {
        return projectService.getProject(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
