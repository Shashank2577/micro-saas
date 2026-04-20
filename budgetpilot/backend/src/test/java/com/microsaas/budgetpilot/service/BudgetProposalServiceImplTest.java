package com.microsaas.budgetpilot.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatResponse;
import com.microsaas.budgetpilot.dto.BudgetProposal;
import com.microsaas.budgetpilot.dto.ProposalRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetProposalServiceImplTest {

    @Mock
    private AiService aiService;

    @InjectMocks
    private BudgetProposalServiceImpl budgetProposalService;

    @Test
    void generateProposal_ShouldReturnProposal() {
        ProposalRequest request = new ProposalRequest();
        request.setDepartment("Engineering");
        request.setFiscalYear(2025);
        request.setGoals("Growth");
        request.setHistoricalData("none");

        when(aiService.chat(any())).thenReturn(new ChatResponse("id", "model", "Here is your budget proposal.", new ChatResponse.Usage(1, 1, 2)));

        BudgetProposal result = budgetProposalService.generateProposal(request);

        assertEquals("Here is your budget proposal.", result.getProposalText());
    }
}
