package com.microsaas.datalineagetracker.controller;

import com.microsaas.datalineagetracker.dto.PolicyDto;
import com.microsaas.datalineagetracker.entity.GovernancePolicy;
import com.microsaas.datalineagetracker.service.GovernanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Governance & Compliance", description = "Endpoints for managing policies and compliance")
public class GovernanceController {
    private final GovernanceService service;

    @GetMapping("/policies")
    @Operation(summary = "List all governance policies")
    public List<GovernancePolicy> getAllPolicies() {
        return service.getAllPolicies();
    }

    @PostMapping("/policies")
    @Operation(summary = "Create a governance policy")
    public GovernancePolicy createPolicy(@RequestBody PolicyDto dto) {
        return service.createPolicy(dto);
    }

    @GetMapping("/compliance/dashboard")
    @Operation(summary = "Get compliance readiness stats")
    public String getComplianceDashboard() {
        // Mock data for dashboard
        return "{\"gdprReady\": 85, \"hipaaReady\": 60, \"ccpaReady\": 90}";
    }
}
