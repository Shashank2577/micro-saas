package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.service.PerformanceAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/people-analytics/performance")
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceAggregationService performanceAggregationService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER', 'EXECUTIVE')")
    public Map<String, Object> getDashboard() {
        return performanceAggregationService.getPerformanceDashboardData();
    }
}
