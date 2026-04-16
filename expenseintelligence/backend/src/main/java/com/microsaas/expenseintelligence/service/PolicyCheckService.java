package com.microsaas.expenseintelligence.service;

import com.microsaas.expenseintelligence.model.*;
import com.microsaas.expenseintelligence.repository.ExpenseRepository;
import com.microsaas.expenseintelligence.repository.PolicyViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyCheckService {

    private final PolicyViolationRepository violationRepository;
    private final ExpenseRepository expenseRepository;

    @Transactional
    public void checkExpense(Expense expense) {
        boolean flagged = false;

        // Rule 1: Meals > 75
        if (expense.getCategory() == ExpenseCategory.MEALS && expense.getAmount().compareTo(new BigDecimal("75.00")) > 0) {
            createViolation(expense, ViolationType.OVER_LIMIT, "Meal expense exceeds $75 limit", Severity.MEDIUM);
            flagged = true;
        }

        // Rule 2: Travel > 500
        if (expense.getCategory() == ExpenseCategory.TRAVEL && expense.getAmount().compareTo(new BigDecimal("500.00")) > 0) {
            createViolation(expense, ViolationType.OVER_LIMIT, "Travel expense exceeds $500 limit", Severity.HIGH);
            flagged = true;
        }

        // Rule 3: Duplicate charge within 7 days
        LocalDate start = expense.getExpenseDate().minusDays(7);
        LocalDate end = expense.getExpenseDate().plusDays(7);
        List<Expense> potentialDuplicates = expenseRepository.findByTenantIdAndVendorAndSubmittedByAndExpenseDateBetween(
                expense.getTenantId(), expense.getVendor(), expense.getSubmittedBy(), start, end);
        
        for (Expense other : potentialDuplicates) {
            if (!other.getId().equals(expense.getId()) && other.getAmount().compareTo(expense.getAmount()) == 0) {
                createViolation(expense, ViolationType.DUPLICATE_CHARGE, "Duplicate charge detected for vendor and amount within 7 days", Severity.HIGH);
                flagged = true;
                break;
            }
        }

        if (flagged) {
            expense.setStatus(ExpenseStatus.FLAGGED);
            expenseRepository.save(expense);
        }
    }

    private void createViolation(Expense expense, ViolationType type, String description, Severity severity) {
        PolicyViolation violation = PolicyViolation.builder()
                .expenseId(expense.getId())
                .violationType(type)
                .description(description)
                .severity(severity)
                .status(ViolationStatus.OPEN)
                .tenantId(expense.getTenantId())
                .build();
        violationRepository.save(violation);
    }
}
