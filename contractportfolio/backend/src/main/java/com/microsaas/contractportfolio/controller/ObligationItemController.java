package com.microsaas.contractportfolio.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contractportfolio.domain.ObligationItem;
import com.microsaas.contractportfolio.service.ObligationItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contracts/obligation-items")
@RequiredArgsConstructor
public class ObligationItemController {

    private final ObligationItemService service;

    @GetMapping
    public List<ObligationItem> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public ObligationItem create(@RequestBody ObligationItem entity) {
        entity.setTenantId(TenantContext.require());
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObligationItem> getById(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ObligationItem> update(@PathVariable UUID id, @RequestBody ObligationItem entity) {
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
