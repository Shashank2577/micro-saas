package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.Transaction;
import com.microsaas.cashflowanalyzer.repository.TransactionRepository;
import com.microsaas.cashflowanalyzer.service.TransactionCategorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionCategorizationService categorizationService;

    @GetMapping
    public ResponseEntity<List<Transaction>> listTransactions(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(transactionRepository.findByTenantId(tenantId));
    }

    @PostMapping("/{id}/categorize")
    public ResponseEntity<Void> categorizeTransaction(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        transactionRepository.findById(id).ifPresent(tx -> {
            if (tx.getTenantId().equals(tenantId)) {
                categorizationService.categorizeTransaction(tx);
            }
        });
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionDetails(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return transactionRepository.findById(id)
                .filter(tx -> tx.getTenantId().equals(tenantId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
