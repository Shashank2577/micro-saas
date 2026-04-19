package com.microsaas.onboardflow.controller;
import com.microsaas.onboardflow.model.TaskAssignment;
import com.microsaas.onboardflow.service.TaskAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/onboarding/task-assignments")
public class TaskAssignmentController {
    private final TaskAssignmentService service;
    public TaskAssignmentController(TaskAssignmentService service) { this.service = service; }
    @GetMapping
    public ResponseEntity<List<TaskAssignment>> findAll(@RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.findAll(tenantId)); }
    @PostMapping
    public ResponseEntity<TaskAssignment> create(@RequestBody TaskAssignment entity, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity, tenantId)); }
    @GetMapping("/{id}")
    public ResponseEntity<TaskAssignment> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.findById(id, tenantId)); }
    @PatchMapping("/{id}")
    public ResponseEntity<TaskAssignment> update(@PathVariable UUID id, @RequestBody TaskAssignment entity, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.update(id, entity, tenantId)); }
    @PostMapping("/{id}/validate")
    public ResponseEntity<?> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok("{\"valid\": true, \"errors\": []}"); }
}
