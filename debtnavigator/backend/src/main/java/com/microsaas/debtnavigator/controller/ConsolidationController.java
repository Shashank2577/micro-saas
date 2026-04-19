package com.microsaas.debtnavigator.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.debtnavigator.entity.ConsolidationOffer;
import com.microsaas.debtnavigator.service.ConsolidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/debt/consolidation-offers")
@RequiredArgsConstructor
public class ConsolidationController {
    private final ConsolidationService service;

    @GetMapping
    public List<ConsolidationOffer> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public ConsolidationOffer create(@RequestBody ConsolidationOffer offer) {
        offer.setTenantId(TenantContext.require());
        return service.create(offer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsolidationOffer> get(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConsolidationOffer> update(@PathVariable UUID id, @RequestBody ConsolidationOffer offer) {
        return service.getById(id, TenantContext.require())
                .map(existing -> {
                    offer.setId(id);
                    offer.setTenantId(TenantContext.require());
                    return ResponseEntity.ok(service.update(offer));
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
