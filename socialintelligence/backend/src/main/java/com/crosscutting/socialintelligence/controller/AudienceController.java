package com.crosscutting.socialintelligence.controller;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import com.crosscutting.socialintelligence.domain.AudienceDemographic;
import com.crosscutting.socialintelligence.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audience")
public class AudienceController {

    private final AnalyticsService analyticsService;

    public AudienceController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/demographics")
    public ResponseEntity<List<AudienceDemographic>> getDemographics(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(analyticsService.getAggregatedDemographics(tenantId));
    }
}
