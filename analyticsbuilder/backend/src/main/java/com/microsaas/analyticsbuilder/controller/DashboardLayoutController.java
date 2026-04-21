package com.microsaas.analyticsbuilder.controller;

import com.microsaas.analyticsbuilder.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboards/{dashboardId}/layout")
@RequiredArgsConstructor
public class DashboardLayoutController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<String> getDashboardLayout(
            @PathVariable UUID dashboardId,
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default-tenant") String tenantId) {

        String layoutConfig = dashboardService.getDashboard(dashboardId, tenantId).getLayoutConfig();
        return ResponseEntity.ok(layoutConfig);
    }
}
