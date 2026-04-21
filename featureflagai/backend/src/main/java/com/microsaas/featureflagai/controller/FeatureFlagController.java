package com.microsaas.featureflagai.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsaas.featureflagai.domain.FeatureFlag;
import com.microsaas.featureflagai.repository.FeatureFlagRepository;
import com.microsaas.featureflagai.service.FlagEvaluationService;
import com.microsaas.featureflagai.service.RolloutService;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flags")
@RequiredArgsConstructor
public class FeatureFlagController {

    private final FeatureFlagRepository featureFlagRepository;
    private final FlagEvaluationService evaluationService;
    private final RolloutService rolloutService;

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
    public ResponseEntity<String> analyzeImpact(
            @PathVariable UUID id,
            @RequestParam String metricsData) {
        String analysis = rolloutService.analyzeImpact(id, metricsData);
        return ResponseEntity.ok(analysis);
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
}
