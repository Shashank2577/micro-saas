package com.microsaas.debtnavigator.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.debtnavigator.entity.OptimizationRun;
import com.microsaas.debtnavigator.service.OptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/debt/optimization-runs")
@RequiredArgsConstructor
public class OptimizationController {
    private final OptimizationService service;

    @GetMapping
    public List<OptimizationRun> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public OptimizationRun create(@RequestBody OptimizationRun run) {
        run.setTenantId(TenantContext.require());
        return service.create(run);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptimizationRun> get(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OptimizationRun> update(@PathVariable UUID id, @RequestBody OptimizationRun run) {
        return service.getById(id, TenantContext.require())
                .map(existing -> {
                    run.setId(id);
                    run.setTenantId(TenantContext.require());
                    return ResponseEntity.ok(service.update(run));
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
