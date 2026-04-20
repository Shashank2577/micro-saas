package com.microsaas.budgetpilot.controller;

import com.microsaas.budgetpilot.dto.BudgetItemRequest;
import com.microsaas.budgetpilot.model.BudgetItem;
import com.microsaas.budgetpilot.service.BudgetItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BudgetItemController {
    private final BudgetItemService budgetItemService;

    @GetMapping("/budgets/{budgetId}/items")
    public ResponseEntity<List<BudgetItem>> getBudgetItems(@PathVariable UUID budgetId) {
        return ResponseEntity.ok(budgetItemService.getBudgetItems(budgetId));
    }

    @PostMapping("/budgets/{budgetId}/items")
    public ResponseEntity<BudgetItem> addBudgetItem(@PathVariable UUID budgetId, @RequestBody BudgetItemRequest request) {
        return ResponseEntity.ok(budgetItemService.addBudgetItem(budgetId, request));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<BudgetItem> updateBudgetItem(@PathVariable UUID id, @RequestBody BudgetItemRequest request) {
        return ResponseEntity.ok(budgetItemService.updateBudgetItem(id, request));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteBudgetItem(@PathVariable UUID id) {
        budgetItemService.deleteBudgetItem(id);
        return ResponseEntity.noContent().build();
    }
}
