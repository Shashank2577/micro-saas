package com.microsaas.budgetpilot.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.queue.QueueService;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetpilot.model.BudgetItem;
import com.microsaas.budgetpilot.model.Expense;
import com.microsaas.budgetpilot.model.Variance;
import com.microsaas.budgetpilot.repository.BudgetItemRepository;
import com.microsaas.budgetpilot.repository.ExpenseRepository;
import com.microsaas.budgetpilot.repository.VarianceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VarianceMonitorServiceImpl implements VarianceMonitorService {
    private final BudgetItemRepository budgetItemRepository;
    private final ExpenseRepository expenseRepository;
    private final VarianceRepository varianceRepository;
    private final AiService aiService;
    private final QueueService queueService;

    @Override
    public Variance calculateVariance(UUID budgetItemId) {
        UUID tenantId = TenantContext.require();
        BudgetItem item = budgetItemRepository.findByIdAndTenantId(budgetItemId, tenantId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        List<Expense> expenses = expenseRepository.findAllByBudgetItemIdAndTenantId(budgetItemId, tenantId);
        BigDecimal actualAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        item.setActualAmount(actualAmount);
        budgetItemRepository.save(item);

        BigDecimal varianceAmount = actualAmount.subtract(item.getAllocatedAmount());
        BigDecimal variancePercentage = BigDecimal.ZERO;

        if (item.getAllocatedAmount().compareTo(BigDecimal.ZERO) > 0) {
            variancePercentage = varianceAmount.divide(item.getAllocatedAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        Variance variance = Variance.builder()
                .tenantId(tenantId)
                .budgetItem(item)
                .varianceAmount(varianceAmount)
                .variancePercentage(variancePercentage)
                .build();

        variance = varianceRepository.save(variance);

        if (variancePercentage.compareTo(new BigDecimal("10")) > 0) {
             queueService.enqueue("budget.variance.alert", "{\"varianceId\":\"" + variance.getId() + "\"}", 0);
        }

        return variance;
    }

    @Override
    public String generateVarianceExplanation(UUID varianceId) {
        Variance variance = varianceRepository.findByIdAndTenantId(varianceId, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Variance not found"));

        BudgetItem item = variance.getBudgetItem();
        String prompt = String.format("Explain the variance for budget item %s. Allocated: %s, Actual: %s. Provide a concise explanation for the overrun.",
                item.getCategory(), item.getAllocatedAmount(), item.getActualAmount());

        ChatResponse response = aiService.chat(new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), null, null));
        String explanation = response.content();

        variance.setExplanation(explanation);
        varianceRepository.save(variance);

        return explanation;
    }
}
