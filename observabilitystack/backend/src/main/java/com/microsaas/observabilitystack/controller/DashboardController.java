package com.microsaas.observabilitystack.controller;

import com.microsaas.observabilitystack.entity.Dashboard;
import com.microsaas.observabilitystack.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboards")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardRepository repository;

    @PostMapping
    public Dashboard create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Dashboard entity) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @GetMapping
    public List<Dashboard> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @PutMapping("/{id}")
    public Dashboard update(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody Dashboard entity) {
        Dashboard existing = repository.findByIdAndTenantId(id, tenantId).orElseThrow();
        existing.setName(entity.getName());
        existing.setUpdatedAt(LocalDateTime.now());
        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        repository.delete(repository.findByIdAndTenantId(id, tenantId).orElseThrow());
    }
}
