package com.microsaas.taxoptimizer.service;

import com.microsaas.taxoptimizer.domain.entity.Expense;
import com.microsaas.taxoptimizer.domain.entity.IncomeSource;
import com.microsaas.taxoptimizer.domain.entity.TaxProfile;
import com.microsaas.taxoptimizer.domain.repository.ExpenseRepository;
import com.microsaas.taxoptimizer.domain.repository.IncomeSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeExpenseService {

    private final IncomeSourceRepository incomeSourceRepository;
    private final ExpenseRepository expenseRepository;

    @Transactional
    public IncomeSource addIncomeSource(UUID tenantId, TaxProfile profile, IncomeSource income) {
        income.setTenantId(tenantId);
        income.setProfile(profile);
        return incomeSourceRepository.save(income);
    }

    @Transactional
    public Expense addExpense(UUID tenantId, TaxProfile profile, Expense expense) {
        expense.setTenantId(tenantId);
        expense.setProfile(profile);
        return expenseRepository.save(expense);
    }

    @Transactional(readOnly = true)
    public List<IncomeSource> getIncomeSources(UUID tenantId, UUID profileId, Integer taxYear) {
        return incomeSourceRepository.findByTenantIdAndProfileIdAndTaxYear(tenantId, profileId, taxYear);
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpenses(UUID tenantId, UUID profileId, Integer taxYear) {
        return expenseRepository.findByTenantIdAndProfileIdAndTaxYear(tenantId, profileId, taxYear);
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getIncomeSummary(UUID tenantId, UUID profileId, Integer taxYear) {
        List<IncomeSource> sources = getIncomeSources(tenantId, profileId, taxYear);
        return sources.stream()
                .collect(Collectors.groupingBy(IncomeSource::getSourceType,
                        Collectors.reducing(BigDecimal.ZERO, IncomeSource::getAmount, BigDecimal::add)));
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getExpenseSummary(UUID tenantId, UUID profileId, Integer taxYear) {
        List<Expense> expenses = getExpenses(tenantId, profileId, taxYear);
        return expenses.stream()
                .collect(Collectors.groupingBy(Expense::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));
    }
}
