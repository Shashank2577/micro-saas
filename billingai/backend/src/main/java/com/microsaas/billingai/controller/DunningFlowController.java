package com.microsaas.billingai.controller;

import com.microsaas.billingai.model.DunningFlow;
import com.microsaas.billingai.service.DunningFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/dunning-flows")
@RequiredArgsConstructor
public class DunningFlowController {
    private final DunningFlowService service;

    @GetMapping
    public List<DunningFlow> findAll(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return service.findAll(tenantId);
    }

    @PostMapping
    public DunningFlow create(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody DunningFlow flow) {
        return service.create(tenantId, flow);
    }

    @GetMapping("/{id}")
    public DunningFlow findById(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return service.findById(tenantId, id);
    }

    @PatchMapping("/{id}")
    public DunningFlow update(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody DunningFlow flow) {
        return service.update(tenantId, id, flow);
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        boolean valid = service.validate(tenantId, id);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}
