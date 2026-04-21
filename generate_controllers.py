import os

base_pkg = 'socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence/controller'

for f in os.listdir(base_pkg):
    if f.endswith('.java'):
        os.remove(os.path.join(base_pkg, f))

with open(f'{base_pkg}/SocialIntelligenceController.java', 'w') as f:
    f.write('''package com.crosscutting.socialintelligence.controller;

import com.crosscutting.socialintelligence.domain.*;
import com.crosscutting.socialintelligence.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SocialIntelligenceController {

    private final OAuthService oAuthService;
    private final AnalyticsService analyticsService;
    private final AIRecommendationService aiRecommendationService;
    private final ContentAnalysisService contentAnalysisService;
    private final MetricsSyncService metricsSyncService;

    // Accounts
    @PostMapping("/accounts/connect")
    public PlatformAccount connectAccount(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestParam String platform, @RequestParam String accountIdExternal) {
        return oAuthService.connectAccount(tenantId, platform, accountIdExternal);
    }

    @GetMapping("/accounts")
    public List<PlatformAccount> getAccounts(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return oAuthService.getAccounts(tenantId);
    }

    @DeleteMapping("/accounts/{id}")
    public void disconnectAccount(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        oAuthService.disconnectAccount(tenantId, id);
    }

    // Metrics & Analytics
    @GetMapping("/metrics/dashboard")
    public Map<String, Object> getDashboardMetrics(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return analyticsService.getDashboardMetrics(tenantId);
    }

    @GetMapping("/metrics/{accountId}")
    public List<Map<String, Object>> getMetricsTrend(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID accountId, @RequestParam(defaultValue = "30") int days) {
        return List.of();
    }

    @PostMapping("/sync")
    public void syncMetrics(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        metricsSyncService.syncMetrics(tenantId);
    }

    // Content
    @GetMapping("/content/top")
    public List<ContentPost> getTopContent(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestParam(required = false) String platform, @RequestParam(defaultValue = "10") int limit) {
        return contentAnalysisService.getTopContent(tenantId, platform, limit);
    }

    // Audience
    @GetMapping("/audience/{accountId}")
    public List<AudienceDemographic> getAudience(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID accountId) {
        return List.of();
    }

    // Growth Recommendations
    @PostMapping("/recommendations/generate")
    public List<GrowthRecommendation> generateRecommendations(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return aiRecommendationService.generateRecommendations(tenantId);
    }

    @GetMapping("/recommendations")
    public List<GrowthRecommendation> getRecommendations(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return aiRecommendationService.getRecommendations(tenantId);
    }

    @PutMapping("/recommendations/{id}/action")
    public GrowthRecommendation actionRecommendation(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return aiRecommendationService.markActioned(id);
    }

    @GetMapping("/growth/projection")
    public Map<String, Object> getGrowthProjection(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestParam UUID accountId, @RequestParam(defaultValue = "30") int days) {
        return Map.of("projectedFollowers", 15000);
    }
}
''')
