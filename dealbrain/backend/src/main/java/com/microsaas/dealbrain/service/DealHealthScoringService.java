package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.DealActivity;
import com.microsaas.dealbrain.model.DealRiskSignal;
import com.microsaas.dealbrain.repository.DealActivityRepository;
import com.microsaas.dealbrain.repository.DealRiskSignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealHealthScoringService {

    private final DealActivityRepository activityRepository;
    private final DealRiskSignalRepository riskSignalRepository;

    /**
     * Calculates health score from 0-100.
     * Starts at 50, adds points for recent activities, subtracts points for risks.
     */
    public int calculateHealthScore(UUID tenantId, UUID dealId) {
        List<DealActivity> activities = activityRepository.findByTenantIdAndDealId(tenantId, dealId);
        List<DealRiskSignal> risks = riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId);

        int score = 50;

        // Activity boosts
        score += Math.min(activities.size() * 5, 40); // max 40 points from activity

        // Risk penalties
        for (DealRiskSignal risk : risks) {
            if ("HIGH".equalsIgnoreCase(risk.getSeverity())) {
                score -= 20;
            } else if ("MEDIUM".equalsIgnoreCase(risk.getSeverity())) {
                score -= 10;
            } else {
                score -= 5;
            }
        }

        return Math.max(0, Math.min(100, score));
    }
}
