package com.microsaas.customersuccessos.controller;

import com.microsaas.customersuccessos.model.Account;
import com.microsaas.customersuccessos.model.HealthScore;
import com.microsaas.customersuccessos.service.CustomerSuccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final CustomerSuccessService service;

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok(service.getAccounts());
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(service.createAccount(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getAccount(id));
    }

    @GetMapping("/{id}/health")
    public ResponseEntity<HealthScore> getLatestHealthScore(@PathVariable UUID id) {
        HealthScore score = service.getLatestHealthScore(id);
        if (score == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(score);
    }

    @PostMapping("/{id}/health")
    public ResponseEntity<HealthScore> recordHealthScore(@PathVariable UUID id, @RequestBody HealthScore score) {
        return ResponseEntity.ok(service.recordHealthScore(id, score));
    }
}
