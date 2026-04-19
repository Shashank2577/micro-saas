package com.microsaas.apievolver.controller;

import com.microsaas.apievolver.model.ApiVersion;
import com.microsaas.apievolver.service.ApiVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/api-evolution/api-versions")
@RequiredArgsConstructor
public class ApiVersionController {
    private final ApiVersionService service;

    @GetMapping
    public ResponseEntity<List<ApiVersion>> getAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @PostMapping
    public ResponseEntity<ApiVersion> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody ApiVersion entity) {
        return ResponseEntity.ok(service.create(entity, tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiVersion> getById(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, tenantId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiVersion> update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody ApiVersion entity) {
        return ResponseEntity.ok(service.update(id, entity, tenantId));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, String>> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        ApiVersion version = service.findById(id, tenantId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "valid");
        return ResponseEntity.ok(response);
    }
}
