package com.microsaas.billingai.controller;

import com.microsaas.billingai.model.RevenueLeakAlert;
import com.microsaas.billingai.service.RevenueLeakAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/revenue-leak-alerts")
@RequiredArgsConstructor
public class RevenueLeakAlertController {
    private final RevenueLeakAlertService service;

    @GetMapping
    public List<RevenueLeakAlert> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.findAll(tenantId);
    }

    @PostMapping
    public RevenueLeakAlert create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody RevenueLeakAlert alert) {
        return service.create(tenantId, alert);
    }

    @GetMapping("/{id}")
    public RevenueLeakAlert findById(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return service.findById(tenantId, id);
    }

    @PatchMapping("/{id}")
    public RevenueLeakAlert update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody RevenueLeakAlert alert) {
        return service.update(tenantId, id, alert);
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        boolean valid = service.validate(tenantId, id);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}
