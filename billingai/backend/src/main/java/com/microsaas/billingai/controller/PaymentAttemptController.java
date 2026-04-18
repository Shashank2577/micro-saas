package com.microsaas.billingai.controller;

import com.microsaas.billingai.model.PaymentAttempt;
import com.microsaas.billingai.service.PaymentAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/payment-attempts")
@RequiredArgsConstructor
public class PaymentAttemptController {
    private final PaymentAttemptService service;

    @GetMapping
    public List<PaymentAttempt> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.findAll(tenantId);
    }

    @PostMapping
    public PaymentAttempt create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody PaymentAttempt attempt) {
        return service.create(tenantId, attempt);
    }

    @GetMapping("/{id}")
    public PaymentAttempt findById(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return service.findById(tenantId, id);
    }

    @PatchMapping("/{id}")
    public PaymentAttempt update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody PaymentAttempt attempt) {
        return service.update(tenantId, id, attempt);
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        boolean valid = service.validate(tenantId, id);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}
