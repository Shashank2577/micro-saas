package com.microsaas.budgetpilot.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetpilot.dto.BudgetCreateRequest;
import com.microsaas.budgetpilot.dto.BudgetUpdateRequest;
import com.microsaas.budgetpilot.model.Budget;
import com.microsaas.budgetpilot.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;

    @Override
    public Budget createBudget(BudgetCreateRequest request) {
        UUID tenantId = TenantContext.require();
        Budget budget = Budget.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .fiscalYear(request.getFiscalYear())
                .totalAmount(request.getTotalAmount())
                .status("DRAFT")
                .build();
        return budgetRepository.save(budget);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Budget> getBudgets() {
        return budgetRepository.findAllByTenantId(TenantContext.require());
    }

    @Override
    @Transactional(readOnly = true)
    public Budget getBudgetById(UUID id) {
        return budgetRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    @Override
    public Budget updateBudget(UUID id, BudgetUpdateRequest request) {
        Budget budget = getBudgetById(id);
        if (request.getName() != null) budget.setName(request.getName());
        if (request.getTotalAmount() != null) budget.setTotalAmount(request.getTotalAmount());
        if (request.getStatus() != null) budget.setStatus(request.getStatus());
        return budgetRepository.save(budget);
    }

    @Override
    public void deleteBudget(UUID id) {
        Budget budget = getBudgetById(id);
        budgetRepository.delete(budget);
    }
}
