package com.microsaas.dealbrain.controller;

import com.microsaas.dealbrain.model.DealActivity;
import com.microsaas.dealbrain.model.DealRecommendation;
import com.microsaas.dealbrain.model.DealRiskSignal;
import com.microsaas.dealbrain.repository.DealActivityRepository;
import com.microsaas.dealbrain.repository.DealRecommendationRepository;
import com.microsaas.dealbrain.repository.DealRiskSignalRepository;
import com.microsaas.dealbrain.service.CloseProbabilityService;
import com.microsaas.dealbrain.service.DealHealthScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealHealthScoringService healthScoringService;
    private final CloseProbabilityService closeProbabilityService;
    private final DealRiskSignalRepository riskSignalRepository;
    private final DealRecommendationRepository recommendationRepository;
    private final DealActivityRepository activityRepository;

    @GetMapping("/{id}/health-score")
    public Map<String, Integer> getHealthScore(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        int score = healthScoringService.calculateHealthScore(tenantId, id);
        return Map.of("healthScore", score);
    }

    @GetMapping("/{id}/close-probability")
    public Map<String, Double> getCloseProbability(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        double prob = closeProbabilityService.estimateCloseProbability(tenantId, id);
        return Map.of("closeProbability", prob);
    }

    @GetMapping("/{id}/risk-signals")
    public List<DealRiskSignal> getRiskSignals(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return riskSignalRepository.findByTenantIdAndDealId(tenantId, id);
    }

    @GetMapping("/{id}/recommendations")
    public List<DealRecommendation> getRecommendations(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return recommendationRepository.findByTenantIdAndDealId(tenantId, id);
    }

    @PostMapping("/{id}/activities")
    public DealActivity addActivity(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody DealActivity activity) {
        activity.setTenantId(tenantId);
        activity.setDealId(id);
        return activityRepository.save(activity);
    }
}
