package com.microsaas.customersuccessos.controller;

import com.microsaas.customersuccessos.model.*;
import com.microsaas.customersuccessos.service.CustomerSuccessService;
import com.microsaas.customersuccessos.service.QbrGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private CustomerSuccessService service;

    @Autowired
    private QbrGenerationService qbrService;

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

    @GetMapping("/{id}/expansion")
    public ResponseEntity<List<ExpansionOpportunity>> getExpansionOpportunities(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getExpansionOpportunities(id));
    }
    
    @GetMapping("/expansion/all")
    public ResponseEntity<List<ExpansionOpportunity>> getAllExpansionOpportunities() {
        return ResponseEntity.ok(service.getAllExpansionOpportunities());
    }

    @PostMapping("/{id}/expansion")
    public ResponseEntity<ExpansionOpportunity> createExpansionOpportunity(@PathVariable UUID id, @RequestBody ExpansionOpportunity opportunity) {
        return ResponseEntity.ok(service.createExpansionOpportunity(id, opportunity));
    }

    @GetMapping("/{id}/qbr")
    public ResponseEntity<List<QbrDeck>> getQbrDecks(@PathVariable UUID id) {
        return ResponseEntity.ok(qbrService.getQbrDecks(id));
    }

    @PostMapping("/{id}/qbr")
    public ResponseEntity<QbrDeck> generateQbr(@PathVariable UUID id) {
        return ResponseEntity.ok(qbrService.generateQbr(id));
    }
}
