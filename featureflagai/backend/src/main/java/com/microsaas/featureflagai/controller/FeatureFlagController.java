package com.microsaas.featureflagai.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsaas.featureflagai.domain.FeatureFlag;
import com.microsaas.featureflagai.domain.FlagSegment;
import com.microsaas.featureflagai.repository.FeatureFlagRepository;
import com.microsaas.featureflagai.service.FlagCleanupService;
import com.microsaas.featureflagai.service.FlagEvaluationService;
import com.microsaas.featureflagai.service.ImpactAnalysisService;
import com.microsaas.featureflagai.service.RolloutService;
import com.microsaas.featureflagai.service.SegmentTargetingService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/flags")
@RequiredArgsConstructor
public class FeatureFlagController {

    private final FeatureFlagRepository featureFlagRepository;
    private final FlagEvaluationService evaluationService;
    private final RolloutService rolloutService;
    private final ImpactAnalysisService impactAnalysisService;
    private final FlagCleanupService cleanupService;
    private final SegmentTargetingService segmentService;

    @GetMapping
    public List<FeatureFlag> listFlags() {
        return featureFlagRepository.findByTenantId(TenantContext.require());
    }

    @PostMapping
    public FeatureFlag createFlag(@RequestBody FeatureFlag flag) {
        flag.setTenantId(TenantContext.require());
        return featureFlagRepository.save(flag);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureFlag> getFlag(@PathVariable UUID id) {
        return featureFlagRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/evaluate")
    public ResponseEntity<Boolean> evaluateFlag(
            @PathVariable UUID id,
            @RequestBody EvaluationRequest request) {
        boolean result = evaluationService.evaluate(id, request.getUserId(), request.getContext());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/rollout")
    public ResponseEntity<Void> controlRollout(
            @PathVariable UUID id,
            @RequestBody RolloutRequest request) {
        rolloutService.controlRollout(id, request.getRolloutPct(), request.isEnabled());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/impact")
    public ResponseEntity<Map<String, String>> analyzeImpact(
            @PathVariable UUID id,
            @RequestParam String metricsData) {
        String analysis = impactAnalysisService.analyzeImpact(id, metricsData);
        return ResponseEntity.ok(Map.of("analysis", analysis));
    }

    @GetMapping("/cleanup-suggestions")
    public ResponseEntity<List<FeatureFlag>> getCleanupSuggestions() {
        return ResponseEntity.ok(cleanupService.getCleanupSuggestions());
    }

    @GetMapping("/{id}/segments")
    public ResponseEntity<List<FlagSegment>> getSegments(@PathVariable UUID id) {
        return ResponseEntity.ok(segmentService.getSegmentsForFlag(id));
    }

    @PostMapping("/{id}/segments")
    public ResponseEntity<FlagSegment> createSegment(
            @PathVariable UUID id,
            @RequestBody FlagSegment segment) {
        return ResponseEntity.ok(segmentService.createSegment(id, segment));
    }

    @PostMapping("/{id}/metrics")
    public ResponseEntity<Void> recordMetrics(
            @PathVariable UUID id,
            @RequestBody MetricsRequest request) {
        rolloutService.recordMetricsAndCheckAutoPause(id, request.getErrorRate());
        return ResponseEntity.ok().build();
    }

    @Data
    public static class EvaluationRequest {
        private String userId;
        private JsonNode context;
    }

    @Data
    public static class RolloutRequest {
        private int rolloutPct;
        private boolean enabled;
    }

    @Data
    public static class MetricsRequest {
        private double errorRate;
    }
}
