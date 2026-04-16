package com.microsaas.hiresignal.service;

import com.microsaas.hiresignal.model.Evaluation;
import com.microsaas.hiresignal.model.HireOutcome;
import com.microsaas.hiresignal.repository.EvaluationRepository;
import com.microsaas.hiresignal.repository.HireOutcomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final HireOutcomeRepository hireOutcomeRepository;

    @Transactional
    public Evaluation submitEval(UUID candidateId, UUID requisitionId, UUID evaluatorId, String scorecard, Integer consistencyScore, Evaluation.Recommendation recommendation) {
        Evaluation evaluation = Evaluation.builder()
                .candidateId(candidateId)
                .requisitionId(requisitionId)
                .evaluatorId(evaluatorId)
                .scorecard(scorecard)
                .consistencyScore(consistencyScore)
                .recommendation(recommendation)
                .build();
        return evaluationRepository.save(evaluation);
    }

    @Transactional
    public HireOutcome recordOutcome(UUID candidateId, boolean hired, Integer retentionMonths, Integer performanceScore) {
        HireOutcome outcome = HireOutcome.builder()
                .candidateId(candidateId)
                .hired(hired)
                .retentionMonths(retentionMonths)
                .performanceScore(performanceScore)
                .build();
        return hireOutcomeRepository.save(outcome);
    }
}
