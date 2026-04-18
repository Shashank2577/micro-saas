package com.microsaas.apievolver.controller;

import com.microsaas.apievolver.model.BreakingChange;
import com.microsaas.apievolver.service.BreakingChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/api-evolution/breaking-changes")
@RequiredArgsConstructor
public class BreakingChangeController {
    private final BreakingChangeService service;

    @GetMapping
    public ResponseEntity<List<BreakingChange>> getAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @PostMapping
    public ResponseEntity<BreakingChange> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody BreakingChange entity) {
        return ResponseEntity.ok(service.create(entity, tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BreakingChange> getById(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, tenantId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BreakingChange> update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody BreakingChange entity) {
        return ResponseEntity.ok(service.update(id, entity, tenantId));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, String>> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        BreakingChange change = service.findById(id, tenantId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "valid");
        return ResponseEntity.ok(response);
    }
}
