package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.Deal;
import com.microsaas.dealbrain.model.HistoricalDeal;
import com.microsaas.dealbrain.repository.DealRepository;
import com.microsaas.dealbrain.repository.HistoricalDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloseProbabilityService {

    private final DealRepository dealRepository;
    private final HistoricalDealRepository historicalDealRepository;
    private final DealHealthScoringService healthScoringService;

    /**
     * Estimates close probability 0.0 - 1.0.
     */
    public double estimateCloseProbability(UUID tenantId, UUID dealId) {
        Deal deal = dealRepository.findById(dealId).orElseThrow();
        int healthScore = healthScoringService.calculateHealthScore(tenantId, dealId);

        List<HistoricalDeal> wonDeals = historicalDealRepository.findByTenantIdAndOutcome(tenantId, "WON");
        List<HistoricalDeal> allDeals = historicalDealRepository.findByTenantId(tenantId);

        double baseWinRate = 0.3; // Default 30% if no history
        if (!allDeals.isEmpty()) {
            baseWinRate = (double) wonDeals.size() / allDeals.size();
        }

        // Adjust probability based on health score.
        // A health score of 50 leaves probability at base win rate.
        // A health score of 100 doubles the probability (capped at 0.95).
        // A health score of 0 halves the probability.
        double healthFactor = (healthScore + 50.0) / 100.0;

        double probability = baseWinRate * healthFactor;

        return Math.max(0.01, Math.min(0.95, probability));
    }
}
