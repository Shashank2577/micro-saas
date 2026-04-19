package com.microsaas.copyoptimizer.controller;

import com.microsaas.copyoptimizer.model.Variant;
import com.microsaas.copyoptimizer.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/copy/variants")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService service;

    @GetMapping
    public ResponseEntity<List<Variant>> list(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.list(tenantId));
    }

    @PostMapping
    public ResponseEntity<Variant> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Variant entity) {
        entity.setTenantId(tenantId);
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Variant> get(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id, tenantId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Variant> update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody Variant entity) {
        return ResponseEntity.ok(service.update(id, tenantId, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        service.validate(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
