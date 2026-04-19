package com.microsaas.complianceradar.controller;

import com.microsaas.complianceradar.domain.TaskAssignment;
import com.microsaas.complianceradar.service.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regulations/task-assignments")
@RequiredArgsConstructor
public class TaskAssignmentController {

    private final TasksService service;

    @GetMapping
    public ResponseEntity<List<TaskAssignment>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskAssignment> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<TaskAssignment> create(@RequestBody TaskAssignment task) {
        return ResponseEntity.ok(service.create(task));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskAssignment> update(@PathVariable UUID id, @RequestBody TaskAssignment updateDetails) {
        return ResponseEntity.ok(service.update(id, updateDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }
}
