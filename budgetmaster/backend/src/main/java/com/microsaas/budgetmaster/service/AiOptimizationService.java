package com.microsaas.budgetmaster.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.budgetmaster.domain.Budget;
import com.microsaas.budgetmaster.domain.Category;
import com.microsaas.budgetmaster.dto.Requests.CategorizeTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiOptimizationService {
    private final AiService aiService;
    private final BudgetService budgetService;

    @Value("${cc.ai.default-model:claude-sonnet-4-6}")
    private String defaultModel;

    public String getOptimizationRecommendations(UUID budgetId) {
        Budget budget = budgetService.getBudget(budgetId);
        List<Category> categories = budgetService.getCategories(budgetId);
        
        String allocationSummary = categories.stream()
                .map(c -> c.getName() + ": $" + c.getAllocatedAmount())
                .collect(Collectors.joining(", "));
        
        String promptText = String.format("Given this budget allocation (Monthly Income: $%s): %s. " +
                "Suggest ways to optimize the budget and increase the savings rate.", 
                budget.getMonthlyIncome(), allocationSummary);

        ChatRequest request = new ChatRequest(
                defaultModel,
                List.of(
                        new ChatMessage("system", "You are a financial advisor specializing in zero-based budgeting."),
                        new ChatMessage("user", promptText)
                ),
                null,
                null
        );

        ChatResponse response = aiService.chat(request);
        return response.content();
    }

    public String suggestCategory(CategorizeTransactionRequest request) {
        String promptText = String.format("Given a transaction description '%s' for amount $%s, " +
                "suggest the most appropriate standard budget category name (e.g., Groceries, Rent, Utilities, Entertainment). " +
                "Return ONLY the category name.", request.getDescription(), request.getAmount());
                
        ChatRequest chatRequest = new ChatRequest(
                defaultModel,
                List.of(
                        new ChatMessage("system", "You are a transaction categorizer."),
                        new ChatMessage("user", promptText)
                ),
                null,
                null
        );

        ChatResponse response = aiService.chat(chatRequest);
        return response.content().trim();
    }
}
