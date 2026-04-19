package com.microsaas.creatoranalytics.controller;

import com.microsaas.creatoranalytics.model.ROISnapshot;
import com.microsaas.creatoranalytics.service.ROISnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/creator-analytics/roisnapshots")
@RequiredArgsConstructor
public class ROISnapshotController {
    private final ROISnapshotService service;

    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000"); // Mock tenant ID for now
    }

    @GetMapping
    public List<ROISnapshot> getAll() {
        return service.findAll(getTenantId());
    }

    @GetMapping("/{id}")
    public ROISnapshot getById(@PathVariable UUID id) {
        return service.findById(id, getTenantId());
    }

    @PostMapping
    public ROISnapshot create(@RequestBody ROISnapshot entity) {
        return service.create(entity, getTenantId());
    }

    @PatchMapping("/{id}")
    public ROISnapshot update(@PathVariable UUID id, @RequestBody ROISnapshot entity) {
        return service.update(id, entity, getTenantId());
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.findById(id, getTenantId());
        return ResponseEntity.ok().build();
    }
}
