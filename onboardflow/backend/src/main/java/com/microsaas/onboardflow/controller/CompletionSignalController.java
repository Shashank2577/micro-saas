package com.microsaas.onboardflow.controller;
import com.microsaas.onboardflow.model.CompletionSignal;
import com.microsaas.onboardflow.service.CompletionSignalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/onboarding/completion-signals")
public class CompletionSignalController {
    private final CompletionSignalService service;
    public CompletionSignalController(CompletionSignalService service) { this.service = service; }
    @GetMapping
    public ResponseEntity<List<CompletionSignal>> findAll(@RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.findAll(tenantId)); }
    @PostMapping
    public ResponseEntity<CompletionSignal> create(@RequestBody CompletionSignal entity, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity, tenantId)); }
    @GetMapping("/{id}")
    public ResponseEntity<CompletionSignal> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.findById(id, tenantId)); }
    @PatchMapping("/{id}")
    public ResponseEntity<CompletionSignal> update(@PathVariable UUID id, @RequestBody CompletionSignal entity, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.update(id, entity, tenantId)); }
    @PostMapping("/{id}/validate")
    public ResponseEntity<?> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok("{\"valid\": true, \"errors\": []}"); }
}
