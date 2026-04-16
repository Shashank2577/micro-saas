package com.microsaas.onboardflow.controller;

import com.microsaas.onboardflow.service.AnalyticsService;
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

    private final AnalyticsService analyticsService;

    @GetMapping("/time-to-productivity")
    public ResponseEntity<Map<String, Double>> getTimeToProductivity() {
        Double avg = analyticsService.getAverageTimeToProductivity();
        return ResponseEntity.ok(Map.of("averageTimeToProductivity", avg));
    }
}
