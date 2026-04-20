package com.microsaas.onboardflow.controller;

import com.microsaas.onboardflow.dto.SystemProvisioningRequestRequest;
import com.microsaas.onboardflow.model.SystemProvisioningRequest;
import com.microsaas.onboardflow.service.SystemProvisioningRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboardflow/provisioning")
@RequiredArgsConstructor
public class SystemProvisioningRequestController {

    private final SystemProvisioningRequestService service;

    private UUID getTenantId(@RequestHeader("X-Tenant-ID") String tenantId) {
        return UUID.fromString(tenantId);
    }

    @GetMapping
    public ResponseEntity<List<SystemProvisioningRequest>> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.findAll(getTenantId(tenantId)));
    }

    @PostMapping
    public ResponseEntity<SystemProvisioningRequest> create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody SystemProvisioningRequestRequest request) {
        return ResponseEntity.ok(service.create(getTenantId(tenantId), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemProvisioningRequest> getById(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, getTenantId(tenantId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SystemProvisioningRequest> update(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody SystemProvisioningRequestRequest request) {
        return ResponseEntity.ok(service.update(id, getTenantId(tenantId), request));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        service.validate(id, getTenantId(tenantId));
        return ResponseEntity.ok().build();
    }
}
