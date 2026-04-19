package com.microsaas.billingai.controller;

import com.microsaas.billingai.model.SubscriptionPlan;
import com.microsaas.billingai.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {
    private final SubscriptionPlanService service;

    @GetMapping
    public List<SubscriptionPlan> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.findAll(tenantId);
    }

    @PostMapping
    public SubscriptionPlan create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody SubscriptionPlan plan) {
        return service.create(tenantId, plan);
    }

    @GetMapping("/{id}")
    public SubscriptionPlan findById(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return service.findById(tenantId, id);
    }

    @PatchMapping("/{id}")
    public SubscriptionPlan update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody SubscriptionPlan plan) {
        return service.update(tenantId, id, plan);
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        boolean valid = service.validate(tenantId, id);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}
