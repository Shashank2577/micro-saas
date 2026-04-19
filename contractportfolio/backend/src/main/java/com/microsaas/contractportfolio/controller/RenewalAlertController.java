package com.microsaas.contractportfolio.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contractportfolio.domain.RenewalAlert;
import com.microsaas.contractportfolio.service.RenewalAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contracts/renewal-alerts")
@RequiredArgsConstructor
public class RenewalAlertController {

    private final RenewalAlertService service;

    @GetMapping
    public List<RenewalAlert> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public RenewalAlert create(@RequestBody RenewalAlert entity) {
        entity.setTenantId(TenantContext.require());
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RenewalAlert> getById(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RenewalAlert> update(@PathVariable UUID id, @RequestBody RenewalAlert entity) {
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
