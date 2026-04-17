package com.microsaas.wealthplan.controller;

import com.microsaas.wealthplan.dto.AIRecommendationDto;
import com.microsaas.wealthplan.dto.DebtPayoffPlanDto;
import com.microsaas.wealthplan.dto.WithdrawalStrategyDto;
import com.microsaas.wealthplan.entity.AllocationRecommendation;
import com.microsaas.wealthplan.service.AssetAllocationService;
import com.microsaas.wealthplan.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final AssetAllocationService allocationService;

    @GetMapping("/ai")
    public ResponseEntity<AIRecommendationDto> getAiRecommendations() {
        return ResponseEntity.ok(recommendationService.getAIRecommendations());
    }

    @GetMapping("/allocation")
    public ResponseEntity<AllocationRecommendation> getAllocationRecommendation(
            @RequestParam int age,
            @RequestParam String riskTolerance) {
        return ResponseEntity.ok(allocationService.optimizeAllocation(age, riskTolerance));
    }

    @GetMapping("/withdrawal-strategy")
    public ResponseEntity<WithdrawalStrategyDto> getWithdrawalStrategy() {
        return ResponseEntity.ok(recommendationService.getWithdrawalStrategy());
    }

    @GetMapping("/debt-payoff")
    public ResponseEntity<DebtPayoffPlanDto> getDebtPayoffPlan() {
        return ResponseEntity.ok(recommendationService.getDebtPayoffPlan());
    }
}
