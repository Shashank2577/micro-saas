package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.dto.ValidateResponse;
import com.microsaas.peopleanalytics.model.EngagementIndicator;
import com.microsaas.peopleanalytics.service.EngagementIndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/engagement-metrics")
@RequiredArgsConstructor
public class EngagementController {

    private final EngagementIndicatorService service;

    @GetMapping
    public ResponseEntity<List<EngagementIndicator>> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EngagementIndicator> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findById(id, tenantId));
    }

    @PostMapping
    public ResponseEntity<EngagementIndicator> create(@RequestBody EngagementIndicator entity, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.create(entity, tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EngagementIndicator> update(@PathVariable UUID id, @RequestBody EngagementIndicator entity, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.update(id, entity, tenantId));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<ValidateResponse> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.validate(id, tenantId));
    }
}
