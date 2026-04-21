package com.microsaas.budgetpilot.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetpilot.dto.BudgetCreateRequest;
import com.microsaas.budgetpilot.dto.BudgetUpdateRequest;
import com.microsaas.budgetpilot.model.Budget;
import com.microsaas.budgetpilot.repository.BudgetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private UUID tenantId;
    private Budget budget;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);

        budget = Budget.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .name("Test Budget")
                .fiscalYear(2024)
                .totalAmount(new BigDecimal("10000.00"))
                .status("DRAFT")
                .build();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void createBudget_ShouldReturnSavedBudget() {
        BudgetCreateRequest request = new BudgetCreateRequest();
        request.setName("New Budget");
        request.setFiscalYear(2025);
        request.setTotalAmount(new BigDecimal("15000.00"));

        when(budgetRepository.save(any(Budget.class))).thenAnswer(i -> {
            Budget b = i.getArgument(0);
            b.setId(UUID.randomUUID());
            return b;
        });

        Budget saved = budgetService.createBudget(request);

        assertNotNull(saved.getId());
        assertEquals("New Budget", saved.getName());
        assertEquals(tenantId, saved.getTenantId());
        assertEquals("DRAFT", saved.getStatus());
        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    void getBudgets_ShouldReturnList() {
        when(budgetRepository.findAllByTenantId(tenantId)).thenReturn(List.of(budget));

        List<Budget> result = budgetService.getBudgets();

        assertEquals(1, result.size());
        assertEquals("Test Budget", result.get(0).getName());
    }

    @Test
    void getBudgetById_WhenExists_ShouldReturnBudget() {
        when(budgetRepository.findByIdAndTenantId(budget.getId(), tenantId)).thenReturn(Optional.of(budget));

        Budget result = budgetService.getBudgetById(budget.getId());

        assertNotNull(result);
        assertEquals(budget.getId(), result.getId());
    }

    @Test
    void getBudgetById_WhenNotExists_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        when(budgetRepository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> budgetService.getBudgetById(id));
    }

    @Test
    void updateBudget_ShouldUpdateFieldsAndSave() {
        when(budgetRepository.findByIdAndTenantId(budget.getId(), tenantId)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        BudgetUpdateRequest request = new BudgetUpdateRequest();
        request.setName("Updated Budget");
        request.setTotalAmount(new BigDecimal("20000.00"));
        request.setStatus("ACTIVE");

        Budget updated = budgetService.updateBudget(budget.getId(), request);

        assertEquals("Updated Budget", updated.getName());
        assertEquals(new BigDecimal("20000.00"), updated.getTotalAmount());
        assertEquals("ACTIVE", updated.getStatus());
    }

    @Test
    void deleteBudget_ShouldCallRepositoryDelete() {
        when(budgetRepository.findByIdAndTenantId(budget.getId(), tenantId)).thenReturn(Optional.of(budget));

        budgetService.deleteBudget(budget.getId());

        verify(budgetRepository).delete(budget);
    }
}
