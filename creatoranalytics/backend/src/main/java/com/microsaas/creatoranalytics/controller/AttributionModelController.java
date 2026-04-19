package com.microsaas.creatoranalytics.controller;

import com.microsaas.creatoranalytics.model.AttributionModel;
import com.microsaas.creatoranalytics.service.AttributionModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/creator-analytics/attribution-models")
@RequiredArgsConstructor
public class AttributionModelController {
    private final AttributionModelService service;

    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000"); // Mock tenant ID for now
    }

    @GetMapping
    public List<AttributionModel> getAll() {
        return service.findAll(getTenantId());
    }

    @GetMapping("/{id}")
    public AttributionModel getById(@PathVariable UUID id) {
        return service.findById(id, getTenantId());
    }

    @PostMapping
    public AttributionModel create(@RequestBody AttributionModel entity) {
        return service.create(entity, getTenantId());
    }

    @PatchMapping("/{id}")
    public AttributionModel update(@PathVariable UUID id, @RequestBody AttributionModel entity) {
        return service.update(id, entity, getTenantId());
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.findById(id, getTenantId());
        return ResponseEntity.ok().build();
    }
}
