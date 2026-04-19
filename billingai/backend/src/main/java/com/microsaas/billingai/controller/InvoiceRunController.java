package com.microsaas.billingai.controller;

import com.microsaas.billingai.model.InvoiceRun;
import com.microsaas.billingai.service.InvoiceRunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/invoice-runs")
@RequiredArgsConstructor
public class InvoiceRunController {
    private final InvoiceRunService service;

    @GetMapping
    public List<InvoiceRun> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.findAll(tenantId);
    }

    @PostMapping
    public InvoiceRun create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody InvoiceRun run) {
        return service.create(tenantId, run);
    }

    @GetMapping("/{id}")
    public InvoiceRun findById(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return service.findById(tenantId, id);
    }

    @PatchMapping("/{id}")
    public InvoiceRun update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody InvoiceRun run) {
        return service.update(tenantId, id, run);
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        boolean valid = service.validate(tenantId, id);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}
