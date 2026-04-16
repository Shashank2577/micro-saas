package com.microsaas.contractsense.service;

import com.microsaas.contractsense.domain.ContractClause;
import com.microsaas.contractsense.domain.RiskAssessment;
import com.microsaas.contractsense.domain.RiskLevel;
import com.microsaas.contractsense.repository.ContractClauseRepository;
import com.microsaas.contractsense.repository.RiskAssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiskAnalysisService {

    private final ContractClauseRepository clauseRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;

    @Transactional
    public RiskAssessment analyzeContract(UUID contractId, UUID tenantId) {
        List<ContractClause> clauses = clauseRepository.findByContractIdAndTenantId(contractId, tenantId);

        int score = 0;
        int flags = 0;
        
        for (ContractClause clause : clauses) {
            if (clause.getRiskLevel() == RiskLevel.HIGH || clause.getRiskLevel() == RiskLevel.CRITICAL) {
                flags++;
            }
            if (clause.isDeviationFromStandard()) {
                score += 10;
            }
            score += getRiskScore(clause.getRiskLevel());
        }

        // Cap score at 100
        int finalScore = Math.min(score, 100);

        RiskAssessment assessment = riskAssessmentRepository.findByContractIdAndTenantId(contractId, tenantId)
                .orElse(RiskAssessment.builder()
                        .contractId(contractId)
                        .tenantId(tenantId)
                        .build());

        assessment.setOverallRiskScore(finalScore);
        assessment.setFlagCount(flags);
        assessment.setMissingProvisionsCount(0); // Mock
        assessment.setRecommendations("Review indemnification and liability cap clauses.");

        return riskAssessmentRepository.save(assessment);
    }

    private int getRiskScore(RiskLevel level) {
        return switch (level) {
            case LOW -> 5;
            case MEDIUM -> 15;
            case HIGH -> 30;
            case CRITICAL -> 50;
        };
    }
}
