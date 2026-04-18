package com.microsaas.identitycore.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.identitycore.model.Anomaly;
import com.microsaas.identitycore.service.AnomalyService;
import com.microsaas.identitycore.service.AIAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/anomalies")
public class AnomalyController {

    private final AnomalyService anomalyService;
    private final AIAnalysisService aiAnalysisService;

    public AnomalyController(AnomalyService anomalyService, AIAnalysisService aiAnalysisService) {
        this.anomalyService = anomalyService;
        this.aiAnalysisService = aiAnalysisService;
    }

    private UUID getTenantId() {
        return UUID.fromString(TenantContext.require().toString());
    }

    @GetMapping
    public ResponseEntity<List<Anomaly>> listAnomalies() {
        return ResponseEntity.ok(anomalyService.getAnomaliesByTenant(getTenantId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anomaly> getAnomaly(@PathVariable UUID id) {
        return anomalyService.getAnomalyById(id, getTenantId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Anomaly> updateAnomalyStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(anomalyService.updateAnomalyStatus(id, getTenantId(), body.get("status")));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeRecentLogs() {
        // Trigger manual analysis - mocking logs JSON for now
        String response = aiAnalysisService.analyzeLogsForAnomalies("[]");
        return ResponseEntity.ok(response);
    }
}
