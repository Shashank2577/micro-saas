package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.*;
import com.microsaas.peopleanalytics.service.PeopleAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PeopleAnalyticsController {

    private final PeopleAnalyticsService service;

    @PostMapping("/headcount/plans")
    public ResponseEntity<HeadcountPlan> createHeadcountPlan(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody HeadcountPlan plan) {
        return ResponseEntity.ok(service.createHeadcountPlan(tenantId, plan));
    }

    @GetMapping("/headcount/plans")
    public ResponseEntity<List<HeadcountPlan>> getHeadcountPlans(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.getHeadcountPlans(tenantId));
    }

    @GetMapping("/headcount/plans/{id}")
    public ResponseEntity<HeadcountPlan> getHeadcountPlan(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.getHeadcountPlan(tenantId, id));
    }

    @PostMapping("/headcount/scenarios/compare")
    public ResponseEntity<Map<String, Object>> compareScenarios(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody Map<String, String> request) {
        UUID s1 = UUID.fromString(request.get("scenario1Id"));
        UUID s2 = UUID.fromString(request.get("scenario2Id"));
        return ResponseEntity.ok(service.compareScenarios(tenantId, s1, s2));
    }

    @GetMapping("/org/health")
    public ResponseEntity<List<OrgHealthMetric>> getOrgHealthMetrics(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.getOrgHealthMetrics(tenantId));
    }

    @GetMapping("/skills/gaps")
    public ResponseEntity<List<SkillsGap>> getSkillsGaps(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam(required = false) String department) {
        return ResponseEntity.ok(service.getSkillsGaps(tenantId, department));
    }

    @PostMapping("/scenarios/model")
    public ResponseEntity<WorkforceScenario> modelScenario(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody WorkforceScenario scenario) {
        return ResponseEntity.ok(service.modelScenario(tenantId, scenario));
    }
}
