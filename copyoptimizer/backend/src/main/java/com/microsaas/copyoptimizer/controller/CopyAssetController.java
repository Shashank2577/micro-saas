package com.microsaas.copyoptimizer.controller;

import com.microsaas.copyoptimizer.model.CopyAsset;
import com.microsaas.copyoptimizer.service.CopyAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/copy/copy-assets")
@RequiredArgsConstructor
public class CopyAssetController {

    private final CopyAssetService service;

    @GetMapping
    public ResponseEntity<List<CopyAsset>> list(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.list(tenantId));
    }

    @PostMapping
    public ResponseEntity<CopyAsset> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody CopyAsset entity) {
        entity.setTenantId(tenantId);
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CopyAsset> get(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id, tenantId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CopyAsset> update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody CopyAsset entity) {
        return ResponseEntity.ok(service.update(id, tenantId, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        service.validate(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
