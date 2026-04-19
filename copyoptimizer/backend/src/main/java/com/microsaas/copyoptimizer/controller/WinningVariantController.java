package com.microsaas.copyoptimizer.controller;

import com.microsaas.copyoptimizer.model.WinningVariant;
import com.microsaas.copyoptimizer.service.WinningVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/copy/winning-variants")
@RequiredArgsConstructor
public class WinningVariantController {

    private final WinningVariantService service;

    @GetMapping
    public ResponseEntity<List<WinningVariant>> list(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.list(tenantId));
    }

    @PostMapping
    public ResponseEntity<WinningVariant> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody WinningVariant entity) {
        entity.setTenantId(tenantId);
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WinningVariant> get(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id, tenantId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WinningVariant> update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody WinningVariant entity) {
        return ResponseEntity.ok(service.update(id, tenantId, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        service.validate(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
