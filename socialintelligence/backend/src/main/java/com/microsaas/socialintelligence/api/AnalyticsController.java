package com.microsaas.socialintelligence.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    @GetMapping("/performance")
    public ResponseEntity<Map<String, Object>> getPerformance() {
        return ResponseEntity.ok(Map.of(
                "totalEngagement", 15000,
                "conversionRate", 3.5,
                "topPlatform", "TWITTER"
        ));
    }
}
