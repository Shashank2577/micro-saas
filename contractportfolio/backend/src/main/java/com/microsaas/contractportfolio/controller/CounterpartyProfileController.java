package com.microsaas.contractportfolio.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contractportfolio.domain.CounterpartyProfile;
import com.microsaas.contractportfolio.service.CounterpartyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contracts/counterparties")
@RequiredArgsConstructor
public class CounterpartyProfileController {

    private final CounterpartyProfileService service;

    @GetMapping
    public List<CounterpartyProfile> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public CounterpartyProfile create(@RequestBody CounterpartyProfile entity) {
        entity.setTenantId(TenantContext.require());
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CounterpartyProfile> getById(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CounterpartyProfile> update(@PathVariable UUID id, @RequestBody CounterpartyProfile entity) {
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
