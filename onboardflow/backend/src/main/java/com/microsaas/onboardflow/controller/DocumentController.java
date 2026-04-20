package com.microsaas.onboardflow.controller;

import com.microsaas.onboardflow.dto.DocumentRequest;
import com.microsaas.onboardflow.model.Document;
import com.microsaas.onboardflow.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboardflow/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;

    private UUID getTenantId(@RequestHeader("X-Tenant-ID") String tenantId) {
        return UUID.fromString(tenantId);
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.findAll(getTenantId(tenantId)));
    }

    @PostMapping
    public ResponseEntity<Document> create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody DocumentRequest request) {
        return ResponseEntity.ok(service.create(getTenantId(tenantId), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getById(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, getTenantId(tenantId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Document> update(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody DocumentRequest request) {
        return ResponseEntity.ok(service.update(id, getTenantId(tenantId), request));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        service.validate(id, getTenantId(tenantId));
        return ResponseEntity.ok().build();
    }
}
