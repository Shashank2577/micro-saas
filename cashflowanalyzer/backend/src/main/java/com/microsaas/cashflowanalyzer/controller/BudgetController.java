package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.Budget;
import com.microsaas.cashflowanalyzer.service.BudgetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetingService budgetingService;

    @GetMapping
    public ResponseEntity<List<Budget>> getBudgets(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(budgetingService.getBudgets(tenantId));
    }

    @PostMapping
    public ResponseEntity<Budget> setBudget(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Budget budget) {
        budget.setTenantId(tenantId);
        return ResponseEntity.ok(budgetingService.setBudget(budget));
    }
}
