package com.microsaas.ghostwriter.controller;

import com.microsaas.ghostwriter.model.Project;
import com.microsaas.ghostwriter.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService service;

    @GetMapping
    public ResponseEntity<List<Project>> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.getAll(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        return service.getById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project, @RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.create(project, tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable UUID id, @RequestBody Project project, @RequestHeader("X-Tenant-ID") String tenantId) {
        try {
            return ResponseEntity.ok(service.update(id, project, tenantId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        service.delete(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
