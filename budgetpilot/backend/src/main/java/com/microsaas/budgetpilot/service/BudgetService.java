package com.microsaas.budgetpilot.service;

import com.microsaas.budgetpilot.dto.BudgetCreateRequest;
import com.microsaas.budgetpilot.dto.BudgetUpdateRequest;
import com.microsaas.budgetpilot.model.Budget;
import java.util.List;
import java.util.UUID;

public interface BudgetService {
    Budget createBudget(BudgetCreateRequest request);
    List<Budget> getBudgets();
    Budget getBudgetById(UUID id);
    Budget updateBudget(UUID id, BudgetUpdateRequest request);
    void deleteBudget(UUID id);
}
