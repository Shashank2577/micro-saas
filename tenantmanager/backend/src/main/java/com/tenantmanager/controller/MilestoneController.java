package com.tenantmanager.controller;

import com.tenantmanager.domain.OnboardingMilestone;
import com.tenantmanager.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/milestones")
@RequiredArgsConstructor
public class MilestoneController {

    private final TenantService tenantService;

    @PutMapping("/{id}/complete")
    public OnboardingMilestone completeMilestone(@PathVariable UUID id) {
        return tenantService.completeMilestone(id);
    }
}
