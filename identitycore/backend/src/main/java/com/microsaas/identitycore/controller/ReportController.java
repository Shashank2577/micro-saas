package com.microsaas.identitycore.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.identitycore.service.AIAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final AIAnalysisService aiAnalysisService;

    public ReportController(AIAnalysisService aiAnalysisService) {
        this.aiAnalysisService = aiAnalysisService;
    }

    private UUID getTenantId() {
        return UUID.fromString(TenantContext.require().toString());
    }

    @GetMapping("/hygiene")
    public ResponseEntity<String> generateHygieneReport() {
        // Mocked system stats
        String report = aiAnalysisService.generateHygieneReport("{\"total_users\": 100, \"open_anomalies\": 5}");
        return ResponseEntity.ok(report);
    }
}
