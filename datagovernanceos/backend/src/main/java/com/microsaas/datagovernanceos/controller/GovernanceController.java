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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Data Governance API", description = "Endpoints for managing data assets, policies, and audits")
public class GovernanceController {

    private final GovernanceService service;
    private final DataClassificationAiService aiService;

    public GovernanceController(GovernanceService service, DataClassificationAiService aiService) {
        this.service = service;
        this.aiService = aiService;
    }

    @Operation(summary = "List all data assets")
    @GetMapping("/assets")
    public List<DataAsset> listAssets() {
        return service.listAssets();
    }

    @Operation(summary = "Get a data asset")
    @GetMapping("/assets/{id}")
    public DataAsset getAsset(@PathVariable UUID id) {
        return service.getAsset(id);
    }

    @Operation(summary = "Register a data asset")
    @PostMapping("/assets")
    public DataAsset createAsset(@RequestBody DataAsset asset) {
        return service.createAsset(asset);
    }

    @Operation(summary = "Update a data asset")
    @PutMapping("/assets/{id}")
    public DataAsset updateAsset(@PathVariable UUID id, @RequestBody DataAsset asset) {
        return service.updateAsset(id, asset);
    }

    @Operation(summary = "Delete a data asset")
    @DeleteMapping("/assets/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable UUID id) {
        service.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all policies")
    @GetMapping("/policies")
    public List<GovernancePolicy> listPolicies() {
        return service.listPolicies();
    }

    @Operation(summary = "Get a policy")
    @GetMapping("/policies/{id}")
    public GovernancePolicy getPolicy(@PathVariable UUID id) {
        return service.getPolicy(id);
    }

    @Operation(summary = "Create a policy")
    @PostMapping("/policies")
    public GovernancePolicy createPolicy(@RequestBody GovernancePolicy policy) {
        return service.createPolicy(policy);
    }

    @Operation(summary = "Update a policy")
    @PutMapping("/policies/{id}")
    public GovernancePolicy updatePolicy(@PathVariable UUID id, @RequestBody GovernancePolicy policy) {
        return service.updatePolicy(id, policy);
    }

    @Operation(summary = "List audits")
    @GetMapping("/audits")
    public List<ComplianceAudit> listAudits() {
        return service.listAudits();
    }

    @Operation(summary = "Run compliance audits for an asset")
    @PostMapping("/audits/run")
    public ResponseEntity<Void> runAudits(@RequestParam UUID assetId) {
        service.runAudits(assetId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "AI classification for an asset")
    @PostMapping("/ai/classify")
    public DataClassificationResult classifyAsset(@RequestBody DataAsset asset) {
        return aiService.classifyAsset(asset);
    }
}
