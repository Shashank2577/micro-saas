package com.microsaas.analyticsbuilder.controller;

import com.microsaas.analyticsbuilder.dto.DashboardWidgetDto;
import com.microsaas.analyticsbuilder.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboards/{dashboardId}/widgets")
@RequiredArgsConstructor
public class DashboardWidgetController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<List<DashboardWidgetDto>> getDashboardWidgets(
            @PathVariable UUID dashboardId,
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default-tenant") String tenantId) {

        List<DashboardWidgetDto> widgets = dashboardService.getDashboard(dashboardId, tenantId).getWidgets();
        return ResponseEntity.ok(widgets);
    }
}
