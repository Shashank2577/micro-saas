package com.microsaas.expenseintelligence.controller;

import com.crosscutting.tenancy.context.TenantContext;
import com.microsaas.expenseintelligence.model.Subscription;
import com.microsaas.expenseintelligence.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{subscriptionId}/redundant")
    public ResponseEntity<Subscription> markRedundant(@PathVariable UUID subscriptionId) {
        return ResponseEntity.ok(subscriptionService.markRedundant(subscriptionId));
    }

    @GetMapping("/spend")
    public ResponseEntity<BigDecimal> getSubscriptionSpend() {
        UUID tenantId = UUID.fromString(TenantContext.getCurrentTenantId());
        return ResponseEntity.ok(subscriptionService.getSubscriptionSpend(tenantId));
    }
}
