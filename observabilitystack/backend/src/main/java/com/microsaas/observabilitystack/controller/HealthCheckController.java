package com.microsaas.observabilitystack.controller;

import com.microsaas.observabilitystack.entity.HealthCheck;
import com.microsaas.observabilitystack.repository.HealthCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthCheckController {
    private final HealthCheckRepository repository;

    @PostMapping("/checks")
    public HealthCheck create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody HealthCheck entity) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @GetMapping("/checks")
    public List<HealthCheck> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @GetMapping("/checks/{id}")
    public HealthCheck getById(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return repository.findByIdAndTenantId(id, tenantId).orElseThrow();
    }
}
