package com.microsaas.copyoptimizer.controller;

import com.microsaas.copyoptimizer.model.AudienceSegment;
import com.microsaas.copyoptimizer.service.AudienceSegmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/copy/audience-segments")
@RequiredArgsConstructor
public class AudienceSegmentController {

    private final AudienceSegmentService service;

    @GetMapping
    public ResponseEntity<List<AudienceSegment>> list(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.list(tenantId));
    }

    @PostMapping
    public ResponseEntity<AudienceSegment> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody AudienceSegment entity) {
        entity.setTenantId(tenantId);
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudienceSegment> get(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id, tenantId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AudienceSegment> update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody AudienceSegment entity) {
        return ResponseEntity.ok(service.update(id, tenantId, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        service.validate(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
