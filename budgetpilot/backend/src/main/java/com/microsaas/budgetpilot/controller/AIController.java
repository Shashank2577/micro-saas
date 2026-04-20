package com.microsaas.budgetpilot.controller;

import com.microsaas.budgetpilot.dto.BudgetProposal;
import com.microsaas.budgetpilot.dto.ProposalRequest;
import com.microsaas.budgetpilot.service.BudgetProposalService;
import com.microsaas.budgetpilot.service.VarianceMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIController {
    private final BudgetProposalService budgetProposalService;
    private final VarianceMonitorService varianceMonitorService;

    @PostMapping("/propose-budget")
    public ResponseEntity<BudgetProposal> proposeBudget(@RequestBody ProposalRequest request) {
        return ResponseEntity.ok(budgetProposalService.generateProposal(request));
    }

    @PostMapping("/explain-variance/{varianceId}")
    public ResponseEntity<String> explainVariance(@PathVariable UUID varianceId) {
        return ResponseEntity.ok(varianceMonitorService.generateVarianceExplanation(varianceId));
    }
}
