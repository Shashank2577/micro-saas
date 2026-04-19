package com.microsaas.legalresearch.controller;

import com.microsaas.legalresearch.domain.PrecedentNote;
import com.microsaas.legalresearch.service.PrecedentNoteService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/research/precedent-notes")
@RequiredArgsConstructor
public class PrecedentNoteController {
    private final PrecedentNoteService service;

    @GetMapping
    public ResponseEntity<List<PrecedentNote>> findAll() {
        return ResponseEntity.ok(service.findAll(TenantContext.require()));
    }

    @PostMapping
    public ResponseEntity<PrecedentNote> create(@RequestBody PrecedentNote entity) {
        entity.setTenantId(TenantContext.require());
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecedentNote> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, TenantContext.require()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PrecedentNote> update(@PathVariable UUID id, @RequestBody PrecedentNote entity) {
        return ResponseEntity.ok(service.update(id, entity, TenantContext.require()));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.validate(id, TenantContext.require()));
    }
}
