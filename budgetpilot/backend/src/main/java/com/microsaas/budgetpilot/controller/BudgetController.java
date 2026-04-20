package com.microsaas.budgetpilot.controller;

import com.microsaas.budgetpilot.dto.BudgetCreateRequest;
import com.microsaas.budgetpilot.dto.BudgetUpdateRequest;
import com.microsaas.budgetpilot.model.Budget;
import com.microsaas.budgetpilot.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<Budget>> getBudgets() {
        return ResponseEntity.ok(budgetService.getBudgets());
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody BudgetCreateRequest request) {
        return ResponseEntity.ok(budgetService.createBudget(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable UUID id) {
        return ResponseEntity.ok(budgetService.getBudgetById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable UUID id, @RequestBody BudgetUpdateRequest request) {
        return ResponseEntity.ok(budgetService.updateBudget(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable UUID id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
