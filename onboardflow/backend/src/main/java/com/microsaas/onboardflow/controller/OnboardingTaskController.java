package com.microsaas.onboardflow.controller;

import com.microsaas.onboardflow.dto.OnboardingTaskRequest;
import com.microsaas.onboardflow.model.OnboardingTask;
import com.microsaas.onboardflow.service.OnboardingTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboardflow/tasks")
@RequiredArgsConstructor
public class OnboardingTaskController {

    private final OnboardingTaskService service;

    private UUID getTenantId(@RequestHeader("X-Tenant-ID") String tenantId) {
        return UUID.fromString(tenantId);
    }

    @GetMapping
    public ResponseEntity<List<OnboardingTask>> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.findAll(getTenantId(tenantId)));
    }

    @PostMapping
    public ResponseEntity<OnboardingTask> create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody OnboardingTaskRequest request) {
        return ResponseEntity.ok(service.create(getTenantId(tenantId), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnboardingTask> getById(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, getTenantId(tenantId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OnboardingTask> update(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody OnboardingTaskRequest request) {
        return ResponseEntity.ok(service.update(id, getTenantId(tenantId), request));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        service.validate(id, getTenantId(tenantId));
        return ResponseEntity.ok().build();
    }
}
