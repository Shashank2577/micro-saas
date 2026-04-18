package com.crosscutting.socialintelligence.controller;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import com.crosscutting.socialintelligence.domain.ContentAnalysis;
import com.crosscutting.socialintelligence.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final AnalyticsService analyticsService;

    public ContentController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/top-performing")
    public ResponseEntity<List<ContentAnalysis>> getTopPerforming(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getTopPerformingContent(tenantId, limit));
    }

    @GetMapping("/analyze-patterns")
    public ResponseEntity<Map<String, String>> analyzePatterns(@RequestHeader("X-Tenant-ID") String tenantId) {
        String analysis = analyticsService.analyzeContentPatterns(tenantId);
        return ResponseEntity.ok(Map.of("analysis", analysis));
    }
}
