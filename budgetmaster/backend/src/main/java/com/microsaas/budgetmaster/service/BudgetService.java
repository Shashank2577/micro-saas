package com.microsaas.budgetmaster.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetmaster.domain.*;
import com.microsaas.budgetmaster.dto.Requests.*;
import com.microsaas.budgetmaster.exception.ResourceNotFoundException;
import com.microsaas.budgetmaster.exception.BadRequestException;
import com.microsaas.budgetmaster.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Budget getBudget(UUID id) {
        return budgetRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
    }

    @Transactional
    public Budget createBudget(CreateBudgetRequest request) {
        Budget budget = Budget.builder()
                .tenantId(TenantContext.require())
                .name(request.getName())
                .monthlyIncome(request.getMonthlyIncome())
                .type(request.getType())
                .rollingMode(request.isRollingMode())
                .month(request.getMonth())
                .year(request.getYear())
                .build();
        return budgetRepository.save(budget);
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories(UUID budgetId) {
        return categoryRepository.findAllByBudgetIdAndTenantId(budgetId, TenantContext.require());
    }

    @Transactional
    public Category createCategory(UUID budgetId, CreateCategoryRequest request) {
        Budget budget = getBudget(budgetId);
        
        List<Category> categories = getCategories(budgetId);
        BigDecimal totalAllocated = categories.stream()
                .map(Category::getAllocatedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (totalAllocated.add(request.getAllocatedAmount()).compareTo(budget.getMonthlyIncome()) > 0) {
            throw new BadRequestException("Category allocation exceeds monthly income");
        }

        Category category = Category.builder()
                .tenantId(TenantContext.require())
                .budgetId(budget.getId())
                .name(request.getName())
                .allocatedAmount(request.getAllocatedAmount())
                .type(request.getType())
                .carryover(request.isCarryover())
                .build();
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(UUID budgetId, UUID categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findByIdAndTenantId(categoryId, TenantContext.require())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (!category.getBudgetId().equals(budgetId)) {
            throw new BadRequestException("Category does not belong to budget");
        }
        category.setName(request.getName());
        category.setAllocatedAmount(request.getAllocatedAmount());
        category.setType(request.getType());
        category.setCarryover(request.isCarryover());
        return categoryRepository.save(category);
    }
}
