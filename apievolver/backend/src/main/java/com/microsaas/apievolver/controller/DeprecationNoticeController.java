package com.microsaas.apievolver.controller;

import com.microsaas.apievolver.model.DeprecationNotice;
import com.microsaas.apievolver.service.DeprecationNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/api-evolution/deprecation-notices")
@RequiredArgsConstructor
public class DeprecationNoticeController {
    private final DeprecationNoticeService service;

    @GetMapping
    public ResponseEntity<List<DeprecationNotice>> getAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @PostMapping
    public ResponseEntity<DeprecationNotice> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody DeprecationNotice entity) {
        return ResponseEntity.ok(service.create(entity, tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeprecationNotice> getById(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, tenantId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeprecationNotice> update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody DeprecationNotice entity) {
        return ResponseEntity.ok(service.update(id, entity, tenantId));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, String>> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        DeprecationNotice notice = service.findById(id, tenantId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "valid");
        return ResponseEntity.ok(response);
    }
}
