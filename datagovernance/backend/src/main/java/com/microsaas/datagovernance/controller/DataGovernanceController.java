package com.microsaas.datagovernance.controller;

import com.microsaas.datagovernance.model.*;
import com.microsaas.datagovernance.dto.*;
import com.microsaas.datagovernance.service.DataGovernanceService;
import com.microsaas.datagovernance.service.PiiDetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Data Governance API", description = "Endpoints for managing policies, consent, and audit logs")
public class DataGovernanceController {

    @Autowired
    private DataGovernanceService service;

    @Autowired
    private PiiDetectionService piiService;

    @Operation(summary = "Get all retention policies")
    @GetMapping("/policies")
    public List<DataRetentionPolicy> getPolicies() {
        return service.getPolicies();
    }

    @Operation(summary = "Create a retention policy")
    @PostMapping("/policies")
    public DataRetentionPolicy createPolicy(@RequestBody DataRetentionPolicy policy) {
        return service.createPolicy(policy);
    }

    @Operation(summary = "Delete a retention policy")
    @DeleteMapping("/policies/{id}")
    public void deletePolicy(@PathVariable UUID id) {
        service.deletePolicy(id);
    }

    @Operation(summary = "Get consent records for an email")
    @GetMapping("/consent/{email}")
    public List<ConsentRecord> getConsent(@PathVariable String email) {
        return service.getConsent(email);
    }

    @Operation(summary = "Create a consent record")
    @PostMapping("/consent")
    public ConsentRecord createConsent(@RequestBody ConsentRecord consent) {
        return service.createConsent(consent);
    }

    @Operation(summary = "Get data lineage for a field")
    @GetMapping("/lineage")
    public List<DataLineageNode> getLineage(@RequestParam String field) {
        return service.getLineage(field);
    }

    @Operation(summary = "Get audit logs")
    @GetMapping("/audit")
    public List<AuditLog> getAuditLogs() {
        return service.getAuditLogs();
    }

    @Operation(summary = "Detect PII in text using AI")
    @PostMapping("/pii/detect")
    public PiiDetectResponse detectPii(@RequestBody PiiDetectRequest request) {
        return piiService.detectPii(request);
    }
}
