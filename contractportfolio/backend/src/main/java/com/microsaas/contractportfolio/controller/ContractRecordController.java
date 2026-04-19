package com.microsaas.contractportfolio.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contractportfolio.domain.ContractRecord;
import com.microsaas.contractportfolio.service.ContractRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contracts/contract-records")
@RequiredArgsConstructor
public class ContractRecordController {

    private final ContractRecordService service;

    @GetMapping
    public List<ContractRecord> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public ContractRecord create(@RequestBody ContractRecord entity) {
        entity.setTenantId(TenantContext.require());
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractRecord> getById(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContractRecord> update(@PathVariable UUID id, @RequestBody ContractRecord entity) {
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
