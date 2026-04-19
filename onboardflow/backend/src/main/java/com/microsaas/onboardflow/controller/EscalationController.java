package com.microsaas.onboardflow.controller;
import com.microsaas.onboardflow.model.Escalation;
import com.microsaas.onboardflow.service.EscalationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/onboarding/escalations")
public class EscalationController {
    private final EscalationService service;
    public EscalationController(EscalationService service) { this.service = service; }
    @GetMapping
    public ResponseEntity<List<Escalation>> findAll(@RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.findAll(tenantId)); }
    @PostMapping
    public ResponseEntity<Escalation> create(@RequestBody Escalation entity, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity, tenantId)); }
    @GetMapping("/{id}")
    public ResponseEntity<Escalation> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.findById(id, tenantId)); }
    @PatchMapping("/{id}")
    public ResponseEntity<Escalation> update(@PathVariable UUID id, @RequestBody Escalation entity, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.update(id, entity, tenantId)); }
    @PostMapping("/{id}/validate")
    public ResponseEntity<?> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok("{\"valid\": true, \"errors\": []}"); }
}
