package com.microsaas.billingai.controller;

import com.microsaas.billingai.model.Subscription;
import com.microsaas.billingai.model.SubscriptionStatus;
import com.microsaas.billingai.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        subscription.setTenantId(tenantId);
        return ResponseEntity.ok(subscriptionService.createSubscription(subscription));
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getSubscriptions(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByTenant(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscription(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(id, tenantId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Subscription> updateStatus(@PathVariable UUID id, @RequestParam SubscriptionStatus status, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(subscriptionService.updateSubscriptionStatus(id, status, tenantId));
    }
}
