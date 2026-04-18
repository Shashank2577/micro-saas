package com.microsaas.insuranceai.controller;

import com.microsaas.insuranceai.domain.Claim;
import com.microsaas.insuranceai.domain.Policy;
import com.microsaas.insuranceai.dto.AnalyticsSummary;
import com.microsaas.insuranceai.service.ClaimService;
import com.microsaas.insuranceai.service.PolicyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final ClaimService claimService;
    private final PolicyService policyService;

    public AnalyticsController(ClaimService claimService, PolicyService policyService) {
        this.claimService = claimService;
        this.policyService = policyService;
    }

    @GetMapping("/summary")
    public AnalyticsSummary getSummary() {
        List<Claim> claims = claimService.getAllClaims();
        List<Policy> policies = policyService.getAllPolicies();

        AnalyticsSummary summary = new AnalyticsSummary();
        summary.setTotalClaims(claims.size());
        summary.setTotalPolicies(policies.size());

        BigDecimal totalFraudScore = BigDecimal.ZERO;
        int claimsWithFraudScore = 0;
        for (Claim claim : claims) {
            if (claim.getAiFraudScore() != null) {
                totalFraudScore = totalFraudScore.add(claim.getAiFraudScore());
                claimsWithFraudScore++;
            }
        }
        if (claimsWithFraudScore > 0) {
            summary.setAverageFraudScore(totalFraudScore.divide(new BigDecimal(claimsWithFraudScore), 2, RoundingMode.HALF_UP));
        } else {
            summary.setAverageFraudScore(BigDecimal.ZERO);
        }

        BigDecimal totalRiskScore = BigDecimal.ZERO;
        int policiesWithRiskScore = 0;
        for (Policy policy : policies) {
            if (policy.getAiRiskScore() != null) {
                totalRiskScore = totalRiskScore.add(policy.getAiRiskScore());
                policiesWithRiskScore++;
            }
        }
        if (policiesWithRiskScore > 0) {
            summary.setAverageRiskScore(totalRiskScore.divide(new BigDecimal(policiesWithRiskScore), 2, RoundingMode.HALF_UP));
        } else {
            summary.setAverageRiskScore(BigDecimal.ZERO);
        }

        return summary;
    }
}
