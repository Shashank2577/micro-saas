package com.crosscutting.socialintelligence.controller;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import com.crosscutting.socialintelligence.domain.GrowthRecommendation;
import com.crosscutting.socialintelligence.service.AIRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/growth")
public class GrowthController {

    private final AIRecommendationService recommendationService;

    public GrowthController(AIRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<GrowthRecommendation>> getRecommendations(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(recommendationService.getRecommendations(tenantId));
    }

    @PostMapping("/recommendations/generate")
    public ResponseEntity<List<GrowthRecommendation>> generateRecommendations(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(recommendationService.generateRecommendations(tenantId));
    }

    @PutMapping("/recommendations/{id}/status")
    public ResponseEntity<GrowthRecommendation> updateStatus(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(recommendationService.updateStatus(id, tenantId, body.get("status")));
    }

    @GetMapping("/projections")
    public ResponseEntity<Map<String, String>> getProjections(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(Map.of("projection", recommendationService.getGrowthProjections(tenantId)));
    }
}
