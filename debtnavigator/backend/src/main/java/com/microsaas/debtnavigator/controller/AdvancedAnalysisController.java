package com.microsaas.debtnavigator.controller;

import com.microsaas.debtnavigator.dto.ConsolidationEvaluationDto;
import com.microsaas.debtnavigator.dto.CreditScoreProjectionDto;
import com.microsaas.debtnavigator.dto.ForgivenessEligibilityDto;
import com.microsaas.debtnavigator.dto.NegotiationRecommendationDto;
import com.microsaas.debtnavigator.service.ConsolidationEvaluationService;
import com.microsaas.debtnavigator.service.CreditScoreProjectionService;
import com.microsaas.debtnavigator.service.ForgivenessCheckerService;
import com.microsaas.debtnavigator.service.NegotiationRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AdvancedAnalysisController {

    private final CreditScoreProjectionService creditScoreProjectionService;
    private final ConsolidationEvaluationService consolidationEvaluationService;
    private final NegotiationRecommendationService negotiationRecommendationService;
    private final ForgivenessCheckerService forgivenessCheckerService;

    @GetMapping("/credit-score")
    public ResponseEntity<CreditScoreProjectionDto> projectCreditScore(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam String strategyType,
            @RequestParam int monthsOut) {
        return ResponseEntity.ok(creditScoreProjectionService.projectScore(tenantId, strategyType, monthsOut));
    }

    @GetMapping("/consolidation")
    public ResponseEntity<List<ConsolidationEvaluationDto>> evaluateConsolidation(
            @RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(consolidationEvaluationService.evaluateConsolidation(tenantId));
    }

    @GetMapping("/negotiation")
    public ResponseEntity<List<NegotiationRecommendationDto>> getNegotiationRecommendations(
            @RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(negotiationRecommendationService.getRecommendations(tenantId));
    }

    @GetMapping("/forgiveness")
    public ResponseEntity<List<ForgivenessEligibilityDto>> checkForgivenessEligibility(
            @RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(forgivenessCheckerService.checkEligibility(tenantId));
    }
}
