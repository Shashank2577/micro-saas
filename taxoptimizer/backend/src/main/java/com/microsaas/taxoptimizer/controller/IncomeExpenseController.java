package com.microsaas.taxoptimizer.controller;

import com.microsaas.taxoptimizer.domain.entity.Expense;
import com.microsaas.taxoptimizer.domain.entity.IncomeSource;
import com.microsaas.taxoptimizer.domain.entity.TaxProfile;
import com.microsaas.taxoptimizer.service.IncomeExpenseService;
import com.microsaas.taxoptimizer.service.TaxProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/finances")
@RequiredArgsConstructor
public class IncomeExpenseController {

    private final IncomeExpenseService incomeExpenseService;
    private final TaxProfileService taxProfileService;

    @PostMapping("/income")
    public ResponseEntity<IncomeSource> addIncomeSource(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId,
            @RequestBody IncomeSource income) {
        TaxProfile profile = taxProfileService.getProfile(tenantId, userId).orElseThrow();
        return ResponseEntity.ok(incomeExpenseService.addIncomeSource(tenantId, profile, income));
    }

    @PostMapping("/expense")
    public ResponseEntity<Expense> addExpense(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId,
            @RequestBody Expense expense) {
        TaxProfile profile = taxProfileService.getProfile(tenantId, userId).orElseThrow();
        return ResponseEntity.ok(incomeExpenseService.addExpense(tenantId, profile, expense));
    }

    @GetMapping("/income-summary")
    public ResponseEntity<?> getIncomeSummary(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId,
            @RequestParam Integer taxYear) {
        TaxProfile profile = taxProfileService.getProfile(tenantId, userId).orElseThrow();
        return ResponseEntity.ok(incomeExpenseService.getIncomeSummary(tenantId, profile.getId(), taxYear));
    }

    @GetMapping("/expense-summary")
    public ResponseEntity<?> getExpenseSummary(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId,
            @RequestParam Integer taxYear) {
        TaxProfile profile = taxProfileService.getProfile(tenantId, userId).orElseThrow();
        return ResponseEntity.ok(incomeExpenseService.getExpenseSummary(tenantId, profile.getId(), taxYear));
    }
}
