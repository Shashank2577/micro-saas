package com.microsaas.billingsync.controller;

import com.microsaas.billingsync.model.Subscription;
import com.microsaas.billingsync.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @PostMapping
    public Subscription createSubscription(@RequestBody CreateSubscriptionRequest request) {
        return subscriptionService.createSubscription(request.planId, request.paymentMethodId);
    }

    @GetMapping("/{id}")
    public Subscription getSubscriptionById(@PathVariable UUID id) {
        return subscriptionService.getSubscriptionById(id);
    }

    @PostMapping("/{id}/change-plan")
    public Subscription changePlan(@PathVariable UUID id, @RequestBody ChangePlanRequest request) {
        return subscriptionService.changePlan(id, request.newPlanId);
    }

    @PostMapping("/{id}/cancel")
    public Subscription cancelSubscription(@PathVariable UUID id) {
        return subscriptionService.cancelSubscription(id);
    }

    public static class CreateSubscriptionRequest {
        public UUID planId;
        public String paymentMethodId;
    }

    public static class ChangePlanRequest {
        public UUID newPlanId;
    }
}
