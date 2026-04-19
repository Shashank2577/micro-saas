package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.dto.ValidateResponse;
import com.microsaas.peopleanalytics.model.*;
import com.microsaas.peopleanalytics.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/people-analytics")
@RequiredArgsConstructor
@Tag(name = "People Analytics", description = "Endpoints for People Analytics domain")
public class PeopleAnalyticsV1Controller {

    private final OrgSnapshotService orgSnapshotService;
    private final HeadcountMetricService headcountMetricService;
    private final AttritionSignalService attritionSignalService;
    private final EngagementIndicatorService engagementIndicatorService;
    private final PerformanceTrendService performanceTrendService;
    private final PlanningScenarioService planningScenarioService;
    private final AiAnalysisService aiAnalysisService;
    private final WorkflowExecutionService workflowExecutionService;

    // --- Org Snapshots ---
    @GetMapping("/org-snapshots")
    @Operation(summary = "List Org Snapshots")
    public ResponseEntity<List<OrgSnapshot>> listOrgSnapshots(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(orgSnapshotService.findAll(tenantId));
    }

    @PostMapping("/org-snapshots")
    @Operation(summary = "Create Org Snapshot")
    public ResponseEntity<OrgSnapshot> createOrgSnapshot(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody OrgSnapshot entity) {
        return ResponseEntity.ok(orgSnapshotService.create(entity, tenantId));
    }

    @GetMapping("/org-snapshots/{id}")
    @Operation(summary = "Get Org Snapshot")
    public ResponseEntity<OrgSnapshot> getOrgSnapshot(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(orgSnapshotService.findById(id, tenantId));
    }

    @PatchMapping("/org-snapshots/{id}")
    @Operation(summary = "Update Org Snapshot")
    public ResponseEntity<OrgSnapshot> updateOrgSnapshot(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody OrgSnapshot entity) {
        return ResponseEntity.ok(orgSnapshotService.update(id, entity, tenantId));
    }

    @PostMapping("/org-snapshots/{id}/validate")
    @Operation(summary = "Validate Org Snapshot")
    public ResponseEntity<ValidateResponse> validateOrgSnapshot(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(orgSnapshotService.validate(id, tenantId));
    }

    // --- Headcount Metrics ---
    @GetMapping("/headcount-metrics")
    public ResponseEntity<List<HeadcountMetric>> listHeadcountMetrics(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(headcountMetricService.findAll(tenantId));
    }

    @PostMapping("/headcount-metrics")
    public ResponseEntity<HeadcountMetric> createHeadcountMetric(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody HeadcountMetric entity) {
        return ResponseEntity.ok(headcountMetricService.create(entity, tenantId));
    }

    @GetMapping("/headcount-metrics/{id}")
    public ResponseEntity<HeadcountMetric> getHeadcountMetric(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(headcountMetricService.findById(id, tenantId));
    }

    @PatchMapping("/headcount-metrics/{id}")
    public ResponseEntity<HeadcountMetric> updateHeadcountMetric(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody HeadcountMetric entity) {
        return ResponseEntity.ok(headcountMetricService.update(id, entity, tenantId));
    }

    @PostMapping("/headcount-metrics/{id}/validate")
    public ResponseEntity<ValidateResponse> validateHeadcountMetric(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(headcountMetricService.validate(id, tenantId));
    }

    // --- Attrition Signals ---
    @GetMapping("/attrition-signals")
    public ResponseEntity<List<AttritionSignal>> listAttritionSignals(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(attritionSignalService.findAll(tenantId));
    }

    @PostMapping("/attrition-signals")
    public ResponseEntity<AttritionSignal> createAttritionSignal(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody AttritionSignal entity) {
        return ResponseEntity.ok(attritionSignalService.create(entity, tenantId));
    }

    @GetMapping("/attrition-signals/{id}")
    public ResponseEntity<AttritionSignal> getAttritionSignal(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(attritionSignalService.findById(id, tenantId));
    }

    @PatchMapping("/attrition-signals/{id}")
    public ResponseEntity<AttritionSignal> updateAttritionSignal(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody AttritionSignal entity) {
        return ResponseEntity.ok(attritionSignalService.update(id, entity, tenantId));
    }

    @PostMapping("/attrition-signals/{id}/validate")
    public ResponseEntity<ValidateResponse> validateAttritionSignal(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(attritionSignalService.validate(id, tenantId));
    }

    // --- Engagement Indicators ---
    @GetMapping("/engagement-indicators")
    public ResponseEntity<List<EngagementIndicator>> listEngagementIndicators(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(engagementIndicatorService.findAll(tenantId));
    }

    @PostMapping("/engagement-indicators")
    public ResponseEntity<EngagementIndicator> createEngagementIndicator(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody EngagementIndicator entity) {
        return ResponseEntity.ok(engagementIndicatorService.create(entity, tenantId));
    }

    @GetMapping("/engagement-indicators/{id}")
    public ResponseEntity<EngagementIndicator> getEngagementIndicator(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(engagementIndicatorService.findById(id, tenantId));
    }

    @PatchMapping("/engagement-indicators/{id}")
    public ResponseEntity<EngagementIndicator> updateEngagementIndicator(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody EngagementIndicator entity) {
        return ResponseEntity.ok(engagementIndicatorService.update(id, entity, tenantId));
    }

    @PostMapping("/engagement-indicators/{id}/validate")
    public ResponseEntity<ValidateResponse> validateEngagementIndicator(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(engagementIndicatorService.validate(id, tenantId));
    }

    // --- Performance Trends ---
    @GetMapping("/performance-trends")
    public ResponseEntity<List<PerformanceTrend>> listPerformanceTrends(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(performanceTrendService.findAll(tenantId));
    }

    @PostMapping("/performance-trends")
    public ResponseEntity<PerformanceTrend> createPerformanceTrend(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody PerformanceTrend entity) {
        return ResponseEntity.ok(performanceTrendService.create(entity, tenantId));
    }

    @GetMapping("/performance-trends/{id}")
    public ResponseEntity<PerformanceTrend> getPerformanceTrend(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(performanceTrendService.findById(id, tenantId));
    }

    @PatchMapping("/performance-trends/{id}")
    public ResponseEntity<PerformanceTrend> updatePerformanceTrend(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody PerformanceTrend entity) {
        return ResponseEntity.ok(performanceTrendService.update(id, entity, tenantId));
    }

    @PostMapping("/performance-trends/{id}/validate")
    public ResponseEntity<ValidateResponse> validatePerformanceTrend(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(performanceTrendService.validate(id, tenantId));
    }

    // --- Planning Scenarios ---
    @GetMapping("/planning-scenarios")
    public ResponseEntity<List<PlanningScenario>> listPlanningScenarios(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(planningScenarioService.findAll(tenantId));
    }

    @PostMapping("/planning-scenarios")
    public ResponseEntity<PlanningScenario> createPlanningScenario(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody PlanningScenario entity) {
        return ResponseEntity.ok(planningScenarioService.create(entity, tenantId));
    }

    @GetMapping("/planning-scenarios/{id}")
    public ResponseEntity<PlanningScenario> getPlanningScenario(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(planningScenarioService.findById(id, tenantId));
    }

    @PatchMapping("/planning-scenarios/{id}")
    public ResponseEntity<PlanningScenario> updatePlanningScenario(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id, @RequestBody PlanningScenario entity) {
        return ResponseEntity.ok(planningScenarioService.update(id, entity, tenantId));
    }

    @PostMapping("/planning-scenarios/{id}/validate")
    public ResponseEntity<ValidateResponse> validatePlanningScenario(@RequestHeader("X-Tenant-ID") UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(planningScenarioService.validate(id, tenantId));
    }

    // --- Global Operations ---
    @PostMapping("/ai/analyze")
    public ResponseEntity<Map<String, String>> aiAnalyze(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(aiAnalysisService.analyze(request, tenantId));
    }

    @PostMapping("/workflows/execute")
    public ResponseEntity<Map<String, String>> executeWorkflow(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(workflowExecutionService.execute(request, tenantId));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> getMetricsSummary(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(Map.of("totalHeadcount", 1500, "attritionRate", 12.5, "engagementScore", 85));
    }
}
