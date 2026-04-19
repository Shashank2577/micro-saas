package com.microsaas.billingai.controller;

import com.microsaas.billingai.model.TaxRule;
import com.microsaas.billingai.service.TaxRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/tax-rules")
@RequiredArgsConstructor
public class TaxRuleController {
    private final TaxRuleService service;

    @GetMapping
    public List<TaxRule> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.findAll(tenantId);
    }

    @PostMapping
    public TaxRule create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody TaxRule rule) {
        return service.create(tenantId, rule);
    }

    @GetMapping("/{id}")
    public TaxRule findById(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return service.findById(tenantId, id);
    }

    @PatchMapping("/{id}")
    public TaxRule update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody TaxRule rule) {
        return service.update(tenantId, id, rule);
    }
}
