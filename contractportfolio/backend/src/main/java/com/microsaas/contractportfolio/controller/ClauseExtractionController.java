package com.microsaas.contractportfolio.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contractportfolio.domain.ClauseExtraction;
import com.microsaas.contractportfolio.service.ClauseExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contracts/clause-extractions")
@RequiredArgsConstructor
public class ClauseExtractionController {

    private final ClauseExtractionService service;

    @GetMapping
    public List<ClauseExtraction> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public ClauseExtraction create(@RequestBody ClauseExtraction entity) {
        entity.setTenantId(TenantContext.require());
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClauseExtraction> getById(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClauseExtraction> update(@PathVariable UUID id, @RequestBody ClauseExtraction entity) {
        return service.update(id, TenantContext.require(), entity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        if (service.validate(id, TenantContext.require())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
