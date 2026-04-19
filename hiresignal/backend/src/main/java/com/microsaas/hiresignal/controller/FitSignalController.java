package com.microsaas.hiresignal.controller;

import com.microsaas.hiresignal.model.FitSignal;
import com.microsaas.hiresignal.service.FitSignalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hiring/fit-signals")
@RequiredArgsConstructor
public class FitSignalController {

    private final FitSignalService service;

    @GetMapping
    public ResponseEntity<List<FitSignal>> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FitSignal> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.findById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FitSignal> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody FitSignal entity) {
        entity.setTenantId(tenantId);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FitSignal> update(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody FitSignal entity) {
        return ResponseEntity.ok(service.update(id, tenantId, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Object>> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.validate(id, tenantId));
    }
}
