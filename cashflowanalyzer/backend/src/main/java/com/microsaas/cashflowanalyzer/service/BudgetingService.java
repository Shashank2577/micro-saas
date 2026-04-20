package com.microsaas.cashflowanalyzer.service;

import com.microsaas.cashflowanalyzer.model.Budget;
import com.microsaas.cashflowanalyzer.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetingService {
    @Autowired
    private BudgetRepository budgetRepository;

    public Budget setBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public List<Budget> getBudgets(String tenantId) {
        return budgetRepository.findByTenantId(tenantId);
    }
}
