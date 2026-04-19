package com.microsaas.hiresignal.controller;

import com.microsaas.hiresignal.model.HiringDecision;
import com.microsaas.hiresignal.service.HiringDecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hiring/hiring-decisions")
@RequiredArgsConstructor
public class HiringDecisionController {

    private final HiringDecisionService service;

    @GetMapping
    public ResponseEntity<List<HiringDecision>> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HiringDecision> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.findById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HiringDecision> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody HiringDecision entity) {
        entity.setTenantId(tenantId);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HiringDecision> update(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody HiringDecision entity) {
        return ResponseEntity.ok(service.update(id, tenantId, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Object>> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.validate(id, tenantId));
    }
}
