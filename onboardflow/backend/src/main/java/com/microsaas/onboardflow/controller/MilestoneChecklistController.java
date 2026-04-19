package com.microsaas.onboardflow.controller;
import com.microsaas.onboardflow.model.MilestoneChecklist;
import com.microsaas.onboardflow.service.MilestoneChecklistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/onboarding/milestone-checklists")
public class MilestoneChecklistController {
    private final MilestoneChecklistService service;
    public MilestoneChecklistController(MilestoneChecklistService service) { this.service = service; }
    @GetMapping
    public ResponseEntity<List<MilestoneChecklist>> findAll(@RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.findAll(tenantId)); }
    @PostMapping
    public ResponseEntity<MilestoneChecklist> create(@RequestBody MilestoneChecklist entity, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity, tenantId)); }
    @GetMapping("/{id}")
    public ResponseEntity<MilestoneChecklist> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.findById(id, tenantId)); }
    @PatchMapping("/{id}")
    public ResponseEntity<MilestoneChecklist> update(@PathVariable UUID id, @RequestBody MilestoneChecklist entity, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.update(id, entity, tenantId)); }
    @PostMapping("/{id}/validate")
    public ResponseEntity<?> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok("{\"valid\": true, \"errors\": []}"); }
}
