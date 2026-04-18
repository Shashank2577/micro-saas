package com.microsaas.budgetmaster.controller;

import com.microsaas.budgetmaster.domain.Transaction;
import com.microsaas.budgetmaster.dto.Requests.CategorizeTransactionRequest;
import com.microsaas.budgetmaster.dto.Requests.CreateTransactionRequest;
import com.microsaas.budgetmaster.service.AiOptimizationService;
import com.microsaas.budgetmaster.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final AiOptimizationService aiOptimizationService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam(required = false) UUID budgetId,
                                                             @RequestParam(required = false) UUID categoryId) {
        if (categoryId != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByCategory(categoryId));
        } else if (budgetId != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByBudget(budgetId));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<Transaction> recordTransaction(@RequestBody CreateTransactionRequest request) {
        return ResponseEntity.ok(transactionService.recordTransaction(request));
    }

    @PostMapping("/categorize")
    public ResponseEntity<Map<String, String>> categorizeTransaction(@RequestBody CategorizeTransactionRequest request) {
        String category = aiOptimizationService.suggestCategory(request);
        return ResponseEntity.ok(Map.of("category", category));
    }
}
