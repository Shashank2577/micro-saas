package com.microsaas.budgetpilot.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetpilot.dto.BudgetItemRequest;
import com.microsaas.budgetpilot.model.Budget;
import com.microsaas.budgetpilot.model.BudgetItem;
import com.microsaas.budgetpilot.repository.BudgetItemRepository;
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
class BudgetItemServiceImplTest {

    @Mock
    private BudgetItemRepository budgetItemRepository;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetItemServiceImpl budgetItemService;

    private UUID tenantId;
    private UUID budgetId;
    private Budget budget;
    private BudgetItem item;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        budgetId = UUID.randomUUID();

        budget = new Budget();
        budget.setId(budgetId);
        budget.setTenantId(tenantId);

        item = BudgetItem.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .budget(budget)
                .category("Software")
                .department("Engineering")
                .allocatedAmount(new BigDecimal("5000.00"))
                .actualAmount(BigDecimal.ZERO)
                .build();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void addBudgetItem_ShouldReturnSavedItem() {
        BudgetItemRequest request = new BudgetItemRequest();
        request.setCategory("Software");
        request.setDepartment("Engineering");
        request.setAllocatedAmount(new BigDecimal("5000.00"));

        when(budgetService.getBudgetById(budgetId)).thenReturn(budget);
        when(budgetItemRepository.save(any(BudgetItem.class))).thenAnswer(i -> {
            BudgetItem b = i.getArgument(0);
            b.setId(UUID.randomUUID());
            return b;
        });

        BudgetItem saved = budgetItemService.addBudgetItem(budgetId, request);

        assertNotNull(saved.getId());
        assertEquals("Software", saved.getCategory());
        assertEquals(budget, saved.getBudget());
        verify(budgetItemRepository).save(any(BudgetItem.class));
    }

    @Test
    void getBudgetItems_ShouldReturnList() {
        when(budgetItemRepository.findAllByBudgetIdAndTenantId(budgetId, tenantId)).thenReturn(List.of(item));

        List<BudgetItem> result = budgetItemService.getBudgetItems(budgetId);

        assertEquals(1, result.size());
        assertEquals("Software", result.get(0).getCategory());
    }

    @Test
    void updateBudgetItem_ShouldUpdateFieldsAndSave() {
        when(budgetItemRepository.findByIdAndTenantId(item.getId(), tenantId)).thenReturn(Optional.of(item));
        when(budgetItemRepository.save(any(BudgetItem.class))).thenReturn(item);

        BudgetItemRequest request = new BudgetItemRequest();
        request.setAllocatedAmount(new BigDecimal("6000.00"));

        BudgetItem updated = budgetItemService.updateBudgetItem(item.getId(), request);

        assertEquals(new BigDecimal("6000.00"), updated.getAllocatedAmount());
    }

    @Test
    void deleteBudgetItem_ShouldCallRepositoryDelete() {
        when(budgetItemRepository.findByIdAndTenantId(item.getId(), tenantId)).thenReturn(Optional.of(item));

        budgetItemService.deleteBudgetItem(item.getId());

        verify(budgetItemRepository).delete(item);
    }
}
