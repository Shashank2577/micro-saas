package com.crosscutting.socialintelligence.controller;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import com.crosscutting.socialintelligence.service.SocialMetricsAggregator;
import com.crosscutting.socialintelligence.dto.UnifiedMetrics;
import com.crosscutting.socialintelligence.dto.EngagementTrends;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final SocialMetricsAggregator metricsAggregator;

    public MetricsController(SocialMetricsAggregator metricsAggregator) {
        this.metricsAggregator = metricsAggregator;
    }

    @PostMapping("/sync")
    public ResponseEntity<Void> syncMetrics(@RequestHeader("X-Tenant-ID") String tenantId) {
        metricsAggregator.syncMetrics(tenantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unified")
    public ResponseEntity<UnifiedMetrics> getUnifiedMetrics(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(metricsAggregator.getUnifiedMetrics(tenantId, startDate, endDate));
    }

    @GetMapping("/engagement-trends")
    public ResponseEntity<EngagementTrends> getEngagementTrends(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(defaultValue = "30") int period) {
        return ResponseEntity.ok(metricsAggregator.getEngagementTrends(tenantId, period));
    }
}
