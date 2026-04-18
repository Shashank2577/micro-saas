package com.tenantmanager.controller;

import com.tenantmanager.domain.CustomerTenant;
import com.tenantmanager.domain.OnboardingMilestone;
import com.tenantmanager.domain.TenantEvent;
import com.tenantmanager.dto.CreateEventRequest;
import com.tenantmanager.dto.CreateMilestoneRequest;
import com.tenantmanager.dto.CreateTenantRequest;
import com.tenantmanager.dto.UpdateStatusRequest;
import com.tenantmanager.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @GetMapping
    public List<CustomerTenant> getTenants() {
        return tenantService.getTenants();
    }

    @PostMapping
    public CustomerTenant createTenant(@RequestBody CreateTenantRequest request) {
        return tenantService.createTenant(request);
    }

    @GetMapping("/{id}")
    public CustomerTenant getTenant(@PathVariable UUID id) {
        return tenantService.getTenant(id);
    }

    @PutMapping("/{id}/status")
    public CustomerTenant updateStatus(@PathVariable UUID id, @RequestBody UpdateStatusRequest request) {
        return tenantService.updateStatus(id, request.getStatus());
    }

    @GetMapping("/{id}/milestones")
    public List<OnboardingMilestone> getMilestones(@PathVariable UUID id) {
        return tenantService.getMilestones(id);
    }

    @PostMapping("/{id}/milestones")
    public OnboardingMilestone createMilestone(@PathVariable UUID id, @RequestBody CreateMilestoneRequest request) {
        return tenantService.createMilestone(id, request);
    }

    @GetMapping("/{id}/events")
    public List<TenantEvent> getEvents(@PathVariable UUID id) {
        return tenantService.getEvents(id);
    }

    @PostMapping("/{id}/events")
    public TenantEvent createEvent(@PathVariable UUID id, @RequestBody CreateEventRequest request) {
        return tenantService.createEvent(id, request);
    }

    @PostMapping("/{id}/analyze-health")
    public CustomerTenant analyzeHealth(@PathVariable UUID id) {
        return tenantService.analyzeHealth(id);
    }
}
