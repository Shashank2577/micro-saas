package com.microsaas.budgetmaster.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetmaster.domain.Budget;
import com.microsaas.budgetmaster.dto.Requests.CreateBudgetRequest;
import com.microsaas.budgetmaster.repository.BudgetRepository;
import com.microsaas.budgetmaster.repository.CategoryRepository;
import com.microsaas.budgetmaster.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BudgetService budgetService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void createBudget_ShouldReturnCreatedBudget() {
        CreateBudgetRequest req = new CreateBudgetRequest();
        req.setName("Test");
        req.setMonthlyIncome(new BigDecimal("5000"));
        req.setType("FIXED");

        Budget b = new Budget();
        b.setId(UUID.randomUUID());
        b.setTenantId(tenantId);
        b.setName("Test");
        b.setMonthlyIncome(new BigDecimal("5000"));
        b.setType("FIXED");

        when(budgetRepository.save(any(Budget.class))).thenReturn(b);

        Budget created = budgetService.createBudget(req);
        assertNotNull(created);
        assertEquals("Test", created.getName());
        assertEquals(tenantId, created.getTenantId());
    }
}
