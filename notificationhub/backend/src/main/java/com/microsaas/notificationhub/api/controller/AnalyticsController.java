package com.microsaas.notificationhub.api.controller;

import com.microsaas.notificationhub.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/delivery-stats")
    public Map<String, Long> getDeliveryStats(@RequestHeader("X-Tenant-ID") String tenantId) {
        return analyticsService.getDeliveryStats(tenantId);
    }

    @GetMapping("/open-rates")
    public Map<String, Double> getOpenRates(@RequestHeader("X-Tenant-ID") String tenantId) {
        return analyticsService.getOpenRates(tenantId);
    }

    @GetMapping("/click-rates")
    public Map<String, Double> getClickRates(@RequestHeader("X-Tenant-ID") String tenantId) {
        return analyticsService.getClickRates(tenantId);
    }
}
