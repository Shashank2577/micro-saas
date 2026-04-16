package com.microsaas.expenseintelligence.controller;

import com.crosscutting.tenancy.context.TenantContext;
import com.microsaas.expenseintelligence.model.Expense;
import com.microsaas.expenseintelligence.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> submitExpense(@RequestBody Expense expense) {
        UUID tenantId = UUID.fromString(TenantContext.getCurrentTenantId());
        expense.setTenantId(tenantId);
        return ResponseEntity.ok(expenseService.submitExpense(expense));
    }

    @PostMapping("/{expenseId}/approve")
    public ResponseEntity<Expense> approveExpense(@PathVariable UUID expenseId) {
        return ResponseEntity.ok(expenseService.approveExpense(expenseId));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> listExpenses() {
        UUID tenantId = UUID.fromString(TenantContext.getCurrentTenantId());
        return ResponseEntity.ok(expenseService.listExpenses(tenantId));
    }

    @GetMapping("/monthly-total")
    public ResponseEntity<BigDecimal> getMonthlyTotal(@RequestParam int year, @RequestParam int month) {
        UUID tenantId = UUID.fromString(TenantContext.getCurrentTenantId());
        return ResponseEntity.ok(expenseService.getMonthlyTotal(tenantId, year, month));
    }
}
