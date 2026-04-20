package com.microsaas.onboardflow.controller;

import com.microsaas.onboardflow.dto.OnboardingFeedbackRequest;
import com.microsaas.onboardflow.model.OnboardingFeedback;
import com.microsaas.onboardflow.service.OnboardingFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboardflow/feedback")
@RequiredArgsConstructor
public class OnboardingFeedbackController {

    private final OnboardingFeedbackService service;

    private UUID getTenantId(@RequestHeader("X-Tenant-ID") String tenantId) {
        return UUID.fromString(tenantId);
    }

    @GetMapping
    public ResponseEntity<List<OnboardingFeedback>> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.findAll(getTenantId(tenantId)));
    }

    @PostMapping
    public ResponseEntity<OnboardingFeedback> create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody OnboardingFeedbackRequest request) {
        return ResponseEntity.ok(service.create(getTenantId(tenantId), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnboardingFeedback> getById(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, getTenantId(tenantId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OnboardingFeedback> update(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody OnboardingFeedbackRequest request) {
        return ResponseEntity.ok(service.update(id, getTenantId(tenantId), request));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        service.validate(id, getTenantId(tenantId));
        return ResponseEntity.ok().build();
    }
}
