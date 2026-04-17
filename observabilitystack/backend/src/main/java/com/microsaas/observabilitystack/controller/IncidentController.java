package com.microsaas.observabilitystack.controller;

import com.microsaas.observabilitystack.entity.Incident;
import com.microsaas.observabilitystack.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {
    private final IncidentRepository repository;

    @PostMapping
    public Incident create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Incident entity) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @GetMapping
    public List<Incident> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @GetMapping("/{id}")
    public Incident getById(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return repository.findByIdAndTenantId(id, tenantId).orElseThrow();
    }

    @PutMapping("/{id}")
    public Incident update(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody Incident entity) {
        Incident existing = repository.findByIdAndTenantId(id, tenantId).orElseThrow();
        existing.setTitle(entity.getTitle());
        existing.setDescription(entity.getDescription());
        existing.setStatus(entity.getStatus());
        existing.setSeverity(entity.getSeverity());
        existing.setUpdatedAt(LocalDateTime.now());
        return repository.save(existing);
    }

    @PostMapping("/{id}/escalate")
    public Incident escalate(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        Incident existing = repository.findByIdAndTenantId(id, tenantId).orElseThrow();
        existing.setSeverity("Critical");
        existing.setUpdatedAt(LocalDateTime.now());
        return repository.save(existing);
    }
}
