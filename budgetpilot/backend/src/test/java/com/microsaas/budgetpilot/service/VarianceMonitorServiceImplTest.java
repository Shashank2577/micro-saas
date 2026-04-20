package com.microsaas.budgetpilot.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.queue.QueueService;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetpilot.model.BudgetItem;
import com.microsaas.budgetpilot.model.Expense;
import com.microsaas.budgetpilot.model.Variance;
import com.microsaas.budgetpilot.repository.BudgetItemRepository;
import com.microsaas.budgetpilot.repository.ExpenseRepository;
import com.microsaas.budgetpilot.repository.VarianceRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VarianceMonitorServiceImplTest {

    @Mock
    private BudgetItemRepository budgetItemRepository;
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private VarianceRepository varianceRepository;
    @Mock
    private AiService aiService;
    @Mock
    private QueueService queueService;

    @InjectMocks
    private VarianceMonitorServiceImpl varianceMonitorService;

    private UUID tenantId;
    private BudgetItem item;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);

        item = new BudgetItem();
        item.setId(UUID.randomUUID());
        item.setAllocatedAmount(new BigDecimal("100.00"));
        item.setActualAmount(BigDecimal.ZERO);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void calculateVariance_ShouldCalculateCorrectlyAndEnqueueAlert() {
        Expense expense = new Expense();
        expense.setAmount(new BigDecimal("120.00"));

        when(budgetItemRepository.findByIdAndTenantId(item.getId(), tenantId)).thenReturn(Optional.of(item));
        when(expenseRepository.findAllByBudgetItemIdAndTenantId(item.getId(), tenantId)).thenReturn(List.of(expense));

        when(varianceRepository.save(any(Variance.class))).thenAnswer(i -> {
            Variance v = i.getArgument(0);
            v.setId(UUID.randomUUID());
            return v;
        });

        Variance variance = varianceMonitorService.calculateVariance(item.getId());

        assertEquals(new BigDecimal("120.00"), item.getActualAmount());
        assertEquals(new BigDecimal("20.00"), variance.getVarianceAmount());
        assertEquals(new BigDecimal("20.0000"), variance.getVariancePercentage());

        verify(queueService).enqueue(eq("budget.variance.alert"), anyString(), eq(0));
    }

    @Test
    void generateVarianceExplanation_ShouldCallAiService() {
        Variance variance = new Variance();
        variance.setId(UUID.randomUUID());
        variance.setBudgetItem(item);

        when(varianceRepository.findByIdAndTenantId(variance.getId(), tenantId)).thenReturn(Optional.of(variance));
        when(aiService.chat(any())).thenReturn(new ChatResponse("id", "model", "Explanation generated.", new ChatResponse.Usage(1, 1, 2)));

        String explanation = varianceMonitorService.generateVarianceExplanation(variance.getId());

        assertEquals("Explanation generated.", explanation);
        assertEquals("Explanation generated.", variance.getExplanation());
        verify(varianceRepository).save(variance);
    }
}
