package com.microsaas.creatoranalytics.controller;

import com.microsaas.creatoranalytics.model.BusinessOutcome;
import com.microsaas.creatoranalytics.model.ContentChannel;
import com.microsaas.creatoranalytics.model.ContentInsight;
import com.microsaas.creatoranalytics.model.ContentPerformance;
import com.microsaas.creatoranalytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/channels")
    public ResponseEntity<ContentChannel> createChannel(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody ContentChannel channel) {
        return ResponseEntity.ok(analyticsService.createChannel(tenantId, channel));
    }

    @GetMapping("/channels")
    public ResponseEntity<List<ContentChannel>> getChannels(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(analyticsService.getChannels(tenantId));
    }

    @PostMapping("/channels/{id}/performance")
    public ResponseEntity<ContentPerformance> ingestPerformance(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id,
            @RequestBody ContentPerformance performance) {
        return ResponseEntity.ok(analyticsService.ingestPerformance(tenantId, id, performance));
    }

    @GetMapping("/channels/{id}/top-content")
    public ResponseEntity<List<ContentPerformance>> getTopContent(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(analyticsService.getTopContent(tenantId, id));
    }

    @PostMapping("/outcomes")
    public ResponseEntity<BusinessOutcome> recordOutcome(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody BusinessOutcome outcome) {
        return ResponseEntity.ok(analyticsService.recordOutcome(tenantId, outcome));
    }

    @GetMapping("/analytics/roi")
    public ResponseEntity<Map<String, Object>> getRoiAnalytics(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(analyticsService.getRoiAnalytics(tenantId));
    }

    @GetMapping("/insights")
    public ResponseEntity<List<ContentInsight>> getInsights(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(analyticsService.getInsights(tenantId));
    }
}
