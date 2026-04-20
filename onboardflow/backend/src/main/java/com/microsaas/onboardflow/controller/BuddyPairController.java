package com.microsaas.onboardflow.controller;

import com.microsaas.onboardflow.dto.BuddyPairRequest;
import com.microsaas.onboardflow.model.BuddyPair;
import com.microsaas.onboardflow.service.BuddyPairService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboardflow/buddy-pairs")
@RequiredArgsConstructor
public class BuddyPairController {

    private final BuddyPairService service;

    private UUID getTenantId(@RequestHeader("X-Tenant-ID") String tenantId) {
        return UUID.fromString(tenantId);
    }

    @GetMapping
    public ResponseEntity<List<BuddyPair>> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.findAll(getTenantId(tenantId)));
    }

    @PostMapping
    public ResponseEntity<BuddyPair> create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody BuddyPairRequest request) {
        return ResponseEntity.ok(service.create(getTenantId(tenantId), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuddyPair> getById(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, getTenantId(tenantId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BuddyPair> update(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody BuddyPairRequest request) {
        return ResponseEntity.ok(service.update(id, getTenantId(tenantId), request));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        service.validate(id, getTenantId(tenantId));
        return ResponseEntity.ok().build();
    }
}
