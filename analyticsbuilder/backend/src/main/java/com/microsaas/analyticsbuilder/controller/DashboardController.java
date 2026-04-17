package com.microsaas.analyticsbuilder.controller;

import com.microsaas.analyticsbuilder.dto.CreateDashboardRequest;
import com.microsaas.analyticsbuilder.dto.DashboardDto;
import com.microsaas.analyticsbuilder.dto.UpdateDashboardRequest;
import com.microsaas.analyticsbuilder.service.DashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboards")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<List<DashboardDto>> getAllDashboards(
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default-tenant") String tenantId) {
        return ResponseEntity.ok(dashboardService.getAllDashboards(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DashboardDto> getDashboard(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default-tenant") String tenantId) {
        return ResponseEntity.ok(dashboardService.getDashboard(id, tenantId));
    }

    @PostMapping
    public ResponseEntity<DashboardDto> createDashboard(
            @Valid @RequestBody CreateDashboardRequest request,
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default-tenant") String tenantId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dashboardService.createDashboard(request, tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DashboardDto> updateDashboard(
            @PathVariable UUID id,
            @RequestBody UpdateDashboardRequest request,
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default-tenant") String tenantId) {
        return ResponseEntity.ok(dashboardService.updateDashboard(id, request, tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDashboard(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default-tenant") String tenantId) {
        dashboardService.deleteDashboard(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
