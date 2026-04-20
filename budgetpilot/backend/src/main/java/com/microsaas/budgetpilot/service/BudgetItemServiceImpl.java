package com.microsaas.budgetpilot.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetpilot.dto.BudgetItemRequest;
import com.microsaas.budgetpilot.model.Budget;
import com.microsaas.budgetpilot.model.BudgetItem;
import com.microsaas.budgetpilot.repository.BudgetItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetItemServiceImpl implements BudgetItemService {
    private final BudgetItemRepository budgetItemRepository;
    private final BudgetService budgetService;

    @Override
    public BudgetItem addBudgetItem(UUID budgetId, BudgetItemRequest request) {
        Budget budget = budgetService.getBudgetById(budgetId);
        UUID tenantId = TenantContext.require();
        BudgetItem item = BudgetItem.builder()
                .tenantId(tenantId)
                .budget(budget)
                .category(request.getCategory())
                .department(request.getDepartment())
                .allocatedAmount(request.getAllocatedAmount())
                .actualAmount(BigDecimal.ZERO)
                .build();
        return budgetItemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetItem> getBudgetItems(UUID budgetId) {
        return budgetItemRepository.findAllByBudgetIdAndTenantId(budgetId, TenantContext.require());
    }

    @Override
    public BudgetItem updateBudgetItem(UUID id, BudgetItemRequest request) {
        BudgetItem item = budgetItemRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (request.getCategory() != null) item.setCategory(request.getCategory());
        if (request.getDepartment() != null) item.setDepartment(request.getDepartment());
        if (request.getAllocatedAmount() != null) item.setAllocatedAmount(request.getAllocatedAmount());
        return budgetItemRepository.save(item);
    }

    @Override
    public void deleteBudgetItem(UUID id) {
        BudgetItem item = budgetItemRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Item not found"));
        budgetItemRepository.delete(item);
    }
}
