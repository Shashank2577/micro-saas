package com.microsaas.onboardflow.controller;
import com.microsaas.onboardflow.model.OnboardingPlan;
import com.microsaas.onboardflow.service.OnboardingPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/onboarding/onboarding-plans")
public class OnboardingPlanController {
    private final OnboardingPlanService service;
    public OnboardingPlanController(OnboardingPlanService service) { this.service = service; }
    @GetMapping
    public ResponseEntity<List<OnboardingPlan>> findAll(@RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.findAll(tenantId)); }
    @PostMapping
    public ResponseEntity<OnboardingPlan> create(@RequestBody OnboardingPlan entity, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity, tenantId)); }
    @GetMapping("/{id}")
    public ResponseEntity<OnboardingPlan> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.findById(id, tenantId)); }
    @PatchMapping("/{id}")
    public ResponseEntity<OnboardingPlan> update(@PathVariable UUID id, @RequestBody OnboardingPlan entity, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok(service.update(id, entity, tenantId)); }
    @PostMapping("/{id}/validate")
    public ResponseEntity<?> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) { return ResponseEntity.ok("{\"valid\": true, \"errors\": []}"); }
}
