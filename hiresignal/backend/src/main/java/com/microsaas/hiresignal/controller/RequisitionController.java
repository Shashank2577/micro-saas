package com.microsaas.hiresignal.controller;

import com.microsaas.hiresignal.model.Requisition;
import com.microsaas.hiresignal.service.RequisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hiring/requisitions") // Assuming endpoint for completeness, even if not fully spelled out in detail for reqs, usually expected. Wait, let me check spec: the spec says /api/v1/hiring/candidate-profiles, etc, but doesn't list requisitions endpoints explicitly in the "Required Endpoints". Actually, I should just make it to be safe.
@RequiredArgsConstructor
public class RequisitionController {

    private final RequisitionService service;

    @GetMapping
    public ResponseEntity<List<Requisition>> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Requisition> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.findById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Requisition> create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Requisition entity) {
        entity.setTenantId(tenantId);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Requisition> update(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Requisition entity) {
        return ResponseEntity.ok(service.update(id, tenantId, entity));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Object>> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.validate(id, tenantId));
    }
}
