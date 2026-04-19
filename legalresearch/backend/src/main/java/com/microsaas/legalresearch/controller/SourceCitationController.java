package com.microsaas.legalresearch.controller;

import com.microsaas.legalresearch.domain.SourceCitation;
import com.microsaas.legalresearch.service.SourceCitationService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/research/source-citations")
@RequiredArgsConstructor
public class SourceCitationController {
    private final SourceCitationService service;

    @GetMapping
    public ResponseEntity<List<SourceCitation>> findAll() {
        return ResponseEntity.ok(service.findAll(TenantContext.require()));
    }

    @PostMapping
    public ResponseEntity<SourceCitation> create(@RequestBody SourceCitation entity) {
        entity.setTenantId(TenantContext.require());
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SourceCitation> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, TenantContext.require()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SourceCitation> update(@PathVariable UUID id, @RequestBody SourceCitation entity) {
        return ResponseEntity.ok(service.update(id, entity, TenantContext.require()));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.validate(id, TenantContext.require()));
    }
}
