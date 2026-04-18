package com.microsaas.debtnavigator.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.debtnavigator.entity.DebtAccount;
import com.microsaas.debtnavigator.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/debt/debt-accounts")
@RequiredArgsConstructor
public class AccountsController {
    private final AccountsService service;

    @GetMapping
    public List<DebtAccount> list() {
        return service.list(TenantContext.require());
    }

    @PostMapping
    public DebtAccount create(@RequestBody DebtAccount account) {
        account.setTenantId(TenantContext.require());
        return service.create(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DebtAccount> get(@PathVariable UUID id) {
        return service.getById(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DebtAccount> update(@PathVariable UUID id, @RequestBody DebtAccount account) {
        return service.getById(id, TenantContext.require())
                .map(existing -> {
                    account.setId(id);
                    account.setTenantId(TenantContext.require());
                    return ResponseEntity.ok(service.update(account));
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
