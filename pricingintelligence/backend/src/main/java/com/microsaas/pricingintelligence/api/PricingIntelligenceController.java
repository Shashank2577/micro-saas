package com.microsaas.pricingintelligence.api;

import com.microsaas.pricingintelligence.domain.*;
import com.microsaas.pricingintelligence.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingIntelligenceController {

    private final SegmentationService segmentationService;
    private final ElasticityModelingService elasticityModelingService;
    private final PriceOptimizationService priceOptimizationService;
    private final ExperimentDesignService experimentDesignService;
    private final WhatIfAnalysisService whatIfAnalysisService;
    private final DiscountAnalysisService discountAnalysisService;

    @GetMapping("/segments")
    public ResponseEntity<List<CustomerSegment>> getSegments() {
        return ResponseEntity.ok(segmentationService.getSegments());
    }

    @PostMapping("/elasticity/calculate")
    public ResponseEntity<ElasticityModel> calculateElasticity(@RequestBody Map<String, String> payload) {
        UUID segmentId = UUID.fromString(payload.get("segmentId"));
        return ResponseEntity.ok(elasticityModelingService.calculateElasticity(segmentId));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<PriceRecommendation>> getRecommendations() {
        return ResponseEntity.ok(priceOptimizationService.getRecommendations());
    }

    @PostMapping("/recommendations/generate")
    public ResponseEntity<PriceRecommendation> generateRecommendation(@RequestBody Map<String, String> payload) {
        UUID segmentId = UUID.fromString(payload.get("segmentId"));
        return ResponseEntity.ok(priceOptimizationService.generateRecommendation(segmentId));
    }

    @GetMapping("/experiments")
    public ResponseEntity<List<PricingExperiment>> getExperiments() {
        return ResponseEntity.ok(experimentDesignService.getExperiments());
    }

    @PostMapping("/experiments")
    public ResponseEntity<PricingExperiment> createExperiment(@RequestBody PricingExperiment experiment) {
        return ResponseEntity.ok(experimentDesignService.createExperiment(experiment));
    }

    @PostMapping("/what-if")
    public ResponseEntity<BigDecimal> simulateRevenue(@RequestBody Map<String, String> payload) {
        UUID segmentId = UUID.fromString(payload.get("segmentId"));
        BigDecimal newPrice = new BigDecimal(payload.get("newPrice"));
        return ResponseEntity.ok(whatIfAnalysisService.simulateRevenue(segmentId, newPrice));
    }

    @GetMapping("/discounts/analyze")
    public ResponseEntity<Map<String, String>> analyzeDiscounts() {
        return ResponseEntity.ok(Map.of("result", discountAnalysisService.analyzeDiscountEffectiveness()));
    }
}
