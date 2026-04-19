package com.microsaas.debtnavigator.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.debtnavigator.entity.PaymentPlan;
import com.microsaas.debtnavigator.service.PlansService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/debt/payment-plans")
@RequiredArgsConstructor
public class PlansController {
    private final PlansService service;

    @GetMapping
    public List<PaymentPlan> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public PaymentPlan create(@RequestBody PaymentPlan plan) {
        plan.setTenantId(TenantContext.require());
        return service.create(plan);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentPlan> get(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentPlan> update(@PathVariable UUID id, @RequestBody PaymentPlan plan) {
        return service.getById(id, TenantContext.require())
                .map(existing -> {
                    plan.setId(id);
                    plan.setTenantId(TenantContext.require());
                    return ResponseEntity.ok(service.update(plan));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.validate(id, TenantContext.require()));
    }

    @PostMapping("/{id}/simulate")
    public ResponseEntity<Object> simulate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.simulate(id, TenantContext.require()));
    }
}
