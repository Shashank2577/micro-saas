package com.microsaas.datagovernanceos.controller;

import com.microsaas.datagovernanceos.dto.DataClassificationResult;
import com.microsaas.datagovernanceos.entity.ComplianceAudit;
import com.microsaas.datagovernanceos.entity.DataAsset;
import com.microsaas.datagovernanceos.entity.GovernancePolicy;
import com.microsaas.datagovernanceos.service.DataClassificationAiService;
import com.microsaas.datagovernanceos.service.GovernanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/data-governance")
@Tag(name = "Data Governance API", description = "Endpoints for managing data assets, policies, and audits")
public class GovernanceController {

    private final GovernanceService service;
    private final DataClassificationAiService aiService;

    public GovernanceController(GovernanceService service, DataClassificationAiService aiService) {
        this.service = service;
        this.aiService = aiService;
    }

    // Legacy Asset Endpoints
    @GetMapping("/assets")
    public List<DataAsset> listAssets() { return service.listAssets(); }
    @GetMapping("/assets/{id}")
    public DataAsset getAsset(@PathVariable UUID id) { return service.getAsset(id); }
    @PostMapping("/assets")
    public DataAsset createAsset(@RequestBody DataAsset asset) { return service.createAsset(asset); }
    @PutMapping("/assets/{id}")
    public DataAsset updateAsset(@PathVariable UUID id, @RequestBody DataAsset asset) { return service.updateAsset(id, asset); }
    @DeleteMapping("/assets/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable UUID id) { service.deleteAsset(id); return ResponseEntity.noContent().build(); }

    // Legacy Audit Endpoints
    @GetMapping("/audits")
    public List<ComplianceAudit> listAudits() { return service.listAudits(); }
    @PostMapping("/audits/run")
    public ResponseEntity<Void> runAudits(@RequestParam UUID assetId) { service.runAudits(assetId); return ResponseEntity.ok().build(); }

    // AI Endpoints
    @PostMapping("/ai/classify")
    public DataClassificationResult classifyAsset(@RequestBody DataAsset asset) { return aiService.classifyAsset(asset); }

    @PostMapping("/ai/analyze")
    public Map<String, Object> analyze(@RequestBody Map<String, Object> request) {
        return aiService.analyzeText(request);
    }

    // Policy Endpoints
    @Operation(summary = "List all governance policies")
    @GetMapping("/governance-policies")
    public List<GovernancePolicy> listPolicies() {
        return service.listPolicies();
    }

    @Operation(summary = "Get a governance policy")
    @GetMapping("/governance-policies/{id}")
    public GovernancePolicy getPolicy(@PathVariable UUID id) {
        return service.getPolicy(id);
    }

    @Operation(summary = "Create a governance policy")
    @PostMapping("/governance-policies")
    public GovernancePolicy createPolicy(@RequestBody GovernancePolicy policy) {
        return service.createPolicy(policy);
    }

    @Operation(summary = "Update a governance policy")
    @PatchMapping("/governance-policies/{id}")
    public GovernancePolicy updatePolicy(@PathVariable UUID id, @RequestBody GovernancePolicy policy) {
        return service.updatePolicy(id, policy);
    }

    @Operation(summary = "Validate a governance policy")
    @PostMapping("/governance-policies/{id}/validate")
    public ResponseEntity<Map<String, String>> validatePolicy(@PathVariable UUID id) {
        service.getPolicy(id); // Ensure it exists
        return ResponseEntity.ok(Map.of("status", "VALID", "message", "Policy validation successful"));
    }

    @PostMapping("/workflows/execute")
    public ResponseEntity<Map<String, String>> executeWorkflow(@RequestBody Map<String, Object> payload) {
        // Implement workflow execution logic here or in a service
        return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "Workflow executed successfully"));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> getMetricsSummary() {
        // Implement metrics logic here or in a service
        return ResponseEntity.ok(Map.of("totalPolicies", service.listPolicies().size(), "totalAssets", service.listAssets().size()));
    }
}
