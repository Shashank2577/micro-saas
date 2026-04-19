package com.microsaas.hiresignal.controller;

import com.microsaas.hiresignal.model.InterviewStage;
import com.microsaas.hiresignal.service.InterviewStageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hiring/interview-stages")
@RequiredArgsConstructor
public class InterviewStageController {

    private final InterviewStageService service;

    @GetMapping
    public ResponseEntity<List<InterviewStage>> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewStage> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.findById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InterviewStage> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody InterviewStage entity) {
        entity.setTenantId(tenantId);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InterviewStage> update(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody InterviewStage entity) {
        return ResponseEntity.ok(service.update(id, tenantId, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Object>> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.validate(id, tenantId));
    }
}
