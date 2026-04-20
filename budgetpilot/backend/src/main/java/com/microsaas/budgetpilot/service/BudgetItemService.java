package com.microsaas.budgetpilot.service;

import com.microsaas.budgetpilot.dto.BudgetItemRequest;
import com.microsaas.budgetpilot.model.BudgetItem;
import java.util.List;
import java.util.UUID;

public interface BudgetItemService {
    BudgetItem addBudgetItem(UUID budgetId, BudgetItemRequest request);
    List<BudgetItem> getBudgetItems(UUID budgetId);
    BudgetItem updateBudgetItem(UUID id, BudgetItemRequest request);
    void deleteBudgetItem(UUID id);
}
