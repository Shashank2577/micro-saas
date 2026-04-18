package com.microsaas.budgetmaster.controller;

import com.microsaas.budgetmaster.domain.Budget;
import com.microsaas.budgetmaster.domain.Category;
import com.microsaas.budgetmaster.dto.Requests.*;
import com.microsaas.budgetmaster.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<Budget>> getBudgets() {
        return ResponseEntity.ok(budgetService.getAllBudgets());
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody CreateBudgetRequest request) {
        return ResponseEntity.ok(budgetService.createBudget(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudget(@PathVariable UUID id) {
        return ResponseEntity.ok(budgetService.getBudget(id));
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<Category>> getCategories(@PathVariable UUID id) {
        return ResponseEntity.ok(budgetService.getCategories(id));
    }

    @PostMapping("/{id}/categories")
    public ResponseEntity<Category> createCategory(@PathVariable UUID id, @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(budgetService.createCategory(id, request));
    }

    @PutMapping("/{id}/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @PathVariable UUID categoryId, @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(budgetService.updateCategory(id, categoryId, request));
    }
    
    @GetMapping("/templates")
    public ResponseEntity<List<String>> getTemplates() {
        return ResponseEntity.ok(List.of("minimalist", "comfort", "luxury"));
    }
}
