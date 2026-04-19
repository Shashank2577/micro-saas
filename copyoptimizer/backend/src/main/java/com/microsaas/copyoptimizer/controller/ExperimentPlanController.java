package com.microsaas.copyoptimizer.controller;

import com.microsaas.copyoptimizer.model.ExperimentPlan;
import com.microsaas.copyoptimizer.service.ExperimentPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/copy/experiment-plans")
@RequiredArgsConstructor
public class ExperimentPlanController {

    private final ExperimentPlanService service;

    @GetMapping
    public ResponseEntity<List<ExperimentPlan>> list(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.list(tenantId));
    }

    @PostMapping
    public ResponseEntity<ExperimentPlan> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody ExperimentPlan entity) {
        entity.setTenantId(tenantId);
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperimentPlan> get(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id, tenantId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExperimentPlan> update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody ExperimentPlan entity) {
        return ResponseEntity.ok(service.update(id, tenantId, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        service.validate(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
