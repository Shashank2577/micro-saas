package com.microsaas.budgetpilot.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.microsaas.budgetpilot.dto.BudgetProposal;
import com.microsaas.budgetpilot.dto.ProposalRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetProposalServiceImpl implements BudgetProposalService {
    private final AiService aiService;

    @Override
    public BudgetProposal generateProposal(ProposalRequest request) {
        String prompt = String.format("Create a budget proposal for %s for FY %d. Business goals: %s. Historical spend: %s. Provide a breakdown of recommended allocations with reasoning.",
                request.getDepartment(), request.getFiscalYear(), request.getGoals(), request.getHistoricalData());

        ChatResponse response = aiService.chat(new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), null, null));

        return BudgetProposal.builder()
                .proposalText(response.content())
                .build();
    }
}
