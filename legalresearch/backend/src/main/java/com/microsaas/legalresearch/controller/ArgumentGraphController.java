package com.microsaas.legalresearch.controller;

import com.microsaas.legalresearch.domain.ArgumentGraph;
import com.microsaas.legalresearch.service.ArgumentGraphService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/research/argument-graphs")
@RequiredArgsConstructor
public class ArgumentGraphController {
    private final ArgumentGraphService service;

    @GetMapping
    public ResponseEntity<List<ArgumentGraph>> findAll() {
        return ResponseEntity.ok(service.findAll(TenantContext.require()));
    }

    @PostMapping
    public ResponseEntity<ArgumentGraph> create(@RequestBody ArgumentGraph entity) {
        entity.setTenantId(TenantContext.require());
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArgumentGraph> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, TenantContext.require()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ArgumentGraph> update(@PathVariable UUID id, @RequestBody ArgumentGraph entity) {
        return ResponseEntity.ok(service.update(id, entity, TenantContext.require()));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.validate(id, TenantContext.require()));
    }
}
