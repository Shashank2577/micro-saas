package com.microsaas.budgetpilot.service;

import com.microsaas.budgetpilot.dto.BudgetProposal;
import com.microsaas.budgetpilot.dto.ProposalRequest;

public interface BudgetProposalService {
    BudgetProposal generateProposal(ProposalRequest request);
}
