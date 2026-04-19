package com.microsaas.legalresearch.controller;

import com.microsaas.legalresearch.domain.MemoDraft;
import com.microsaas.legalresearch.service.MemoDraftService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/research/memo-drafts")
@RequiredArgsConstructor
public class MemoDraftController {
    private final MemoDraftService service;

    @GetMapping
    public ResponseEntity<List<MemoDraft>> findAll() {
        return ResponseEntity.ok(service.findAll(TenantContext.require()));
    }

    @PostMapping
    public ResponseEntity<MemoDraft> create(@RequestBody MemoDraft entity) {
        entity.setTenantId(TenantContext.require());
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoDraft> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, TenantContext.require()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MemoDraft> update(@PathVariable UUID id, @RequestBody MemoDraft entity) {
        return ResponseEntity.ok(service.update(id, entity, TenantContext.require()));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.validate(id, TenantContext.require()));
    }
}
