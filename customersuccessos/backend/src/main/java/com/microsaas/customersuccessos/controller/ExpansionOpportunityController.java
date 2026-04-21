package com.microsaas.customersuccessos.controller;

import com.microsaas.customersuccessos.model.ExpansionOpportunity;
import com.microsaas.customersuccessos.service.CustomerSuccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expansion-opportunities")
@RequiredArgsConstructor
public class ExpansionOpportunityController {

    private final CustomerSuccessService service;

    @GetMapping
    public ResponseEntity<List<ExpansionOpportunity>> getAllExpansionOpportunities() {
        return ResponseEntity.ok(service.getAllExpansionOpportunities());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<ExpansionOpportunity>> getExpansionOpportunities(@PathVariable UUID accountId) {
        return ResponseEntity.ok(service.getExpansionOpportunities(accountId));
    }

    @PostMapping("/account/{accountId}")
    public ResponseEntity<ExpansionOpportunity> createExpansionOpportunity(
            @PathVariable UUID accountId,
            @RequestBody ExpansionOpportunity opportunity) {
        return ResponseEntity.ok(service.createExpansionOpportunity(accountId, opportunity));
    }
}
