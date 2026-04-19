package com.microsaas.debtnavigator.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.debtnavigator.entity.RiskProjection;
import com.microsaas.debtnavigator.service.ProjectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/debt/risk-projections")
@RequiredArgsConstructor
public class ProjectionsController {
    private final ProjectionsService service;

    @GetMapping
    public List<RiskProjection> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public RiskProjection create(@RequestBody RiskProjection projection) {
        projection.setTenantId(TenantContext.require());
        return service.create(projection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiskProjection> get(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RiskProjection> update(@PathVariable UUID id, @RequestBody RiskProjection projection) {
        return service.getById(id, TenantContext.require())
                .map(existing -> {
                    projection.setId(id);
                    projection.setTenantId(TenantContext.require());
                    return ResponseEntity.ok(service.update(projection));
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
