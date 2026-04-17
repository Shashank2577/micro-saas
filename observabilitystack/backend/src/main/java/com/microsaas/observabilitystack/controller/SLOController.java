package com.microsaas.observabilitystack.controller;

import com.microsaas.observabilitystack.entity.SLO;
import com.microsaas.observabilitystack.entity.SLOCompliance;
import com.microsaas.observabilitystack.repository.SLORepository;
import com.microsaas.observabilitystack.repository.SLOComplianceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/slos")
@RequiredArgsConstructor
public class SLOController {
    private final SLORepository repository;
    private final SLOComplianceRepository complianceRepository;

    @PostMapping
    public SLO create(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody SLO entity) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @GetMapping
    public List<SLO> getAll(@RequestHeader("X-Tenant-ID") String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @GetMapping("/{id}")
    public SLO getById(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return repository.findByIdAndTenantId(id, tenantId).orElseThrow();
    }

    @GetMapping("/{id}/compliance")
    public List<SLOCompliance> getCompliance(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return complianceRepository.findByTenantId(tenantId);
    }
}
