package com.microsaas.wealthplan.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.wealthplan.dto.AIRecommendationDto;
import com.microsaas.wealthplan.dto.DebtPayoffPlanDto;
import com.microsaas.wealthplan.dto.WithdrawalStrategyDto;
import com.microsaas.wealthplan.entity.DebtItem;
import com.microsaas.wealthplan.repository.DebtItemRepository;
import com.microsaas.wealthplan.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final AiService aiService;
    private final GoalRepository goalRepository;
    private final DebtItemRepository debtRepository;

    public AIRecommendationDto getAIRecommendations() {
        String tenantId = TenantContext.require().toString();
        long goalCount = goalRepository.findByTenantId(tenantId).size();

        String prompt = String.format(
            "User has %d goals. Generate 3 short financial advice points.", goalCount
        );

        ChatRequest request = new ChatRequest("gemma2:9b", List.of(new ChatMessage("user", prompt)), null, null);
        ChatResponse chatResponse = aiService.chat(request);
        String aiResponse = chatResponse.content();

        return new AIRecommendationDto(List.of(aiResponse.split("\n")));
    }

    public WithdrawalStrategyDto getWithdrawalStrategy() {
        return new WithdrawalStrategyDto("Consider drawing down taxable accounts first, followed by tax-deferred, and finally tax-free (Roth) to optimize long-term tax efficiency.");
    }

    public DebtPayoffPlanDto getDebtPayoffPlan() {
        String tenantId = TenantContext.require().toString();
        List<DebtItem> debts = debtRepository.findByTenantId(tenantId);

        // Avalanche method
        debts.sort(Comparator.comparing(DebtItem::getInterestRate).reversed());

        List<UUID> order = debts.stream().map(DebtItem::getId).collect(Collectors.toList());
        return new DebtPayoffPlanDto("AVALANCHE", order, 48); // Mocked 48 months
    }
}
