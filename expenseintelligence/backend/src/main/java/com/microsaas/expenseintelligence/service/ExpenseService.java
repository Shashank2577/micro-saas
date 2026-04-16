package com.microsaas.expenseintelligence.service;

import com.microsaas.expenseintelligence.model.Expense;
import com.microsaas.expenseintelligence.model.ExpenseStatus;
import com.microsaas.expenseintelligence.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final PolicyCheckService policyCheckService;

    @Transactional
    public Expense submitExpense(Expense expense) {
        expense.setStatus(ExpenseStatus.PENDING);
        Expense saved = expenseRepository.save(expense);
        policyCheckService.checkExpense(saved);
        return saved;
    }

    @Transactional
    public Expense approveExpense(UUID expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        expense.setStatus(ExpenseStatus.APPROVED);
        return expenseRepository.save(expense);
    }

    public List<Expense> listExpenses(UUID tenantId) {
        return expenseRepository.findByTenantId(tenantId);
    }

    public BigDecimal getMonthlyTotal(UUID tenantId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        List<Expense> expenses = expenseRepository.findByTenantIdAndExpenseDateBetween(tenantId, start, end);
        return expenses.stream()
                .filter(e -> e.getStatus() == ExpenseStatus.APPROVED)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
