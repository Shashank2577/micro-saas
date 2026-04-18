package com.microsaas.engagementpulse.controller;

import com.microsaas.engagementpulse.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Analytics", description = "Engagement Analytics API")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/analytics/teams/{teamId}")
    @Operation(summary = "Get team analytics")
    public ResponseEntity<Map<String, Object>> getTeamAnalytics(@PathVariable UUID teamId) {
        return ResponseEntity.ok(analyticsService.getTeamAnalytics(teamId));
    }

    @GetMapping("/analytics/trends")
    @Operation(summary = "Get trends")
    public ResponseEntity<Map<String, Object>> getTrends() {
        return ResponseEntity.ok(analyticsService.getTrends());
    }

    @GetMapping("/reports/export")
    @Operation(summary = "Export engagement report")
    public ResponseEntity<byte[]> exportReport() {
        String csvContent = analyticsService.generateExportData();
        byte[] bytes = csvContent.getBytes();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "engagement_report.csv");
        headers.setContentLength(bytes.length);

        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
