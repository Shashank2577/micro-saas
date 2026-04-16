package com.microsaas.taxdataorganizer.controller;

import com.microsaas.taxdataorganizer.model.Transaction;
import com.microsaas.taxdataorganizer.model.TransactionCategory;
import com.microsaas.taxdataorganizer.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        transaction.setTenantId(tenantId);
        return ResponseEntity.ok(transactionService.addTransaction(transaction));
    }

    @PutMapping("/{id}/category")
    public ResponseEntity<Transaction> categorizeTransaction(@PathVariable UUID id, @RequestParam TransactionCategory category, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(transactionService.categorizeTransaction(id, category, tenantId));
    }

    @GetMapping("/tax-years/{taxYearId}/deductible-total")
    public ResponseEntity<BigDecimal> getDeductibleTotal(@PathVariable UUID taxYearId, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(transactionService.getDeductibleTotal(taxYearId, tenantId));
    }
}
