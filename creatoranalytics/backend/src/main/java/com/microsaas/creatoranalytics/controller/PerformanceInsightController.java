package com.microsaas.creatoranalytics.controller;

import com.microsaas.creatoranalytics.model.PerformanceInsight;
import com.microsaas.creatoranalytics.service.PerformanceInsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/creator-analytics/performance-insights")
@RequiredArgsConstructor
public class PerformanceInsightController {
    private final PerformanceInsightService service;

    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000"); // Mock tenant ID for now
    }

    @GetMapping
    public List<PerformanceInsight> getAll() {
        return service.findAll(getTenantId());
    }

    @GetMapping("/{id}")
    public PerformanceInsight getById(@PathVariable UUID id) {
        return service.findById(id, getTenantId());
    }

    @PostMapping
    public PerformanceInsight create(@RequestBody PerformanceInsight entity) {
        return service.create(entity, getTenantId());
    }

    @PatchMapping("/{id}")
    public PerformanceInsight update(@PathVariable UUID id, @RequestBody PerformanceInsight entity) {
        return service.update(id, entity, getTenantId());
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.findById(id, getTenantId());
        return ResponseEntity.ok().build();
    }
}
