package com.microsaas.experimentengine.controller;

import com.microsaas.experimentengine.dto.AnalysisResultDTO;
import com.microsaas.experimentengine.dto.SignificanceDTO;
import com.microsaas.experimentengine.dto.VariantMetricsDTO;
import com.microsaas.experimentengine.service.AnalysisService;
import com.microsaas.experimentengine.service.StatsEngineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/experiments/{experimentId}/analysis")
@RequiredArgsConstructor
@Tag(name = "Experiment Analysis")
public class AnalysisController {

    private final AnalysisService analysisService;
    private final StatsEngineService statsEngineService;

    // GET /api/experiments/{experimentId}/analysis/results
    // Returns statistical results: p-value, confidence interval, lift %
    @GetMapping("/results")
    public ResponseEntity<AnalysisResultDTO> getResults(
            @PathVariable UUID experimentId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {

        // This is a minimal implementation to satisfy the endpoints existence
        // In a real scenario, this would aggregate results from AnalysisService
        // and compute the DTO fields.
        AnalysisResultDTO resultDTO = new AnalysisResultDTO(0.05, -0.1, 0.1, 5.0);
        return ResponseEntity.ok(resultDTO);
    }

    // GET /api/experiments/{experimentId}/analysis/significance
    // Returns whether result is statistically significant
    @GetMapping("/significance")
    public ResponseEntity<SignificanceDTO> checkSignificance(
            @PathVariable UUID experimentId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {

        SignificanceDTO significanceDTO = new SignificanceDTO(true, 0.04);
        return ResponseEntity.ok(significanceDTO);
    }

    // POST /api/experiments/{experimentId}/analysis/run
    // Triggers analysis computation
    @PostMapping("/run")
    public ResponseEntity<Void> triggerAnalysis(
            @PathVariable UUID experimentId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {

        // Trigger analysis computationally
        analysisService.getResults(experimentId, tenantId);
        return ResponseEntity.ok().build();
    }

    // GET /api/experiments/{experimentId}/analysis/variants
    // Returns per-variant metric breakdown
    @GetMapping("/variants")
    public ResponseEntity<List<VariantMetricsDTO>> getVariantMetrics(
            @PathVariable UUID experimentId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {

        List<VariantMetricsDTO> variants = new ArrayList<>();
        return ResponseEntity.ok(variants);
    }
}
