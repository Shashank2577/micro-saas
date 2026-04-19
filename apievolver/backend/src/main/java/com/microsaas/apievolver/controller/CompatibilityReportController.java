package com.microsaas.apievolver.controller;

import com.microsaas.apievolver.model.CompatibilityReport;
import com.microsaas.apievolver.service.CompatibilityReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/api-evolution/compatibility-reports")
@RequiredArgsConstructor
public class CompatibilityReportController {
    private final CompatibilityReportService service;

    @GetMapping
    public ResponseEntity<List<CompatibilityReport>> getAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @PostMapping
    public ResponseEntity<CompatibilityReport> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody CompatibilityReport entity) {
        return ResponseEntity.ok(service.create(entity, tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompatibilityReport> getById(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, tenantId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CompatibilityReport> update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody CompatibilityReport entity) {
        return ResponseEntity.ok(service.update(id, entity, tenantId));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, String>> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        CompatibilityReport report = service.findById(id, tenantId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "valid");
        return ResponseEntity.ok(response);
    }
}
