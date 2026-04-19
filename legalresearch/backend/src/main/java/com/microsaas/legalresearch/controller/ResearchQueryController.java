package com.microsaas.legalresearch.controller;

import com.microsaas.legalresearch.domain.ResearchQuery;
import com.microsaas.legalresearch.service.ResearchQueryService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/research/research-querys")
@RequiredArgsConstructor
public class ResearchQueryController {
    private final ResearchQueryService service;

    @GetMapping
    public ResponseEntity<List<ResearchQuery>> findAll() {
        return ResponseEntity.ok(service.findAll(TenantContext.require()));
    }

    @PostMapping
    public ResponseEntity<ResearchQuery> create(@RequestBody ResearchQuery entity) {
        entity.setTenantId(TenantContext.require());
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResearchQuery> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, TenantContext.require()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResearchQuery> update(@PathVariable UUID id, @RequestBody ResearchQuery entity) {
        return ResponseEntity.ok(service.update(id, entity, TenantContext.require()));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.validate(id, TenantContext.require()));
    }
}
