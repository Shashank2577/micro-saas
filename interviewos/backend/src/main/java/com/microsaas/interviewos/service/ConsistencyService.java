package com.microsaas.interviewos.service;

import com.microsaas.interviewos.model.EvaluatorConsistency;
import com.microsaas.interviewos.model.InterviewScorecard;
import com.microsaas.interviewos.repository.EvaluatorConsistencyRepository;
import com.microsaas.interviewos.repository.InterviewScorecardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsistencyService {

    private final EvaluatorConsistencyRepository consistencyRepository;
    private final InterviewScorecardRepository scorecardRepository;

    @Transactional
    public EvaluatorConsistency recalculateConsistency(UUID interviewerId, UUID tenantId) {
        List<InterviewScorecard> scorecards = scorecardRepository.findByInterviewerIdAndTenantId(interviewerId, tenantId);
        
        int totalEvaluations = scorecards.size();
        double averageScore = 0.0;
        
        if (totalEvaluations > 0) {
            double sum = 0;
            for (InterviewScorecard scorecard : scorecards) {
                sum += scorecard.getOverallScore();
            }
            averageScore = sum / totalEvaluations;
        }

        EvaluatorConsistency consistency = consistencyRepository.findByInterviewerIdAndTenantId(interviewerId, tenantId)
                .orElse(EvaluatorConsistency.builder()
                        .id(UUID.randomUUID())
                        .interviewerId(interviewerId)
                        .tenantId(tenantId)
                        .build());

        consistency.setAverageScore(averageScore);
        consistency.setTotalEvaluations(totalEvaluations);

        return consistencyRepository.save(consistency);
    }
}
