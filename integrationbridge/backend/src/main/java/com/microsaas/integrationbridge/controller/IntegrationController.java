package com.microsaas.integrationbridge.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.integrationbridge.dto.*;
import com.microsaas.integrationbridge.model.AuditLog;
import com.microsaas.integrationbridge.model.FieldMapping;
import com.microsaas.integrationbridge.model.Integration;
import com.microsaas.integrationbridge.model.SyncJob;
import com.microsaas.integrationbridge.service.AuditService;
import com.microsaas.integrationbridge.service.CredentialService;
import com.microsaas.integrationbridge.service.IntegrationService;
import com.microsaas.integrationbridge.service.SyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/integrations")
public class IntegrationController {

    private final IntegrationService integrationService;
    private final CredentialService credentialService;
    private final SyncService syncService;
    private final AuditService auditService;

    public IntegrationController(IntegrationService integrationService, CredentialService credentialService, SyncService syncService, AuditService auditService) {
        this.integrationService = integrationService;
        this.credentialService = credentialService;
        this.syncService = syncService;
        this.auditService = auditService;
    }

    private UUID getTenantId() {
        return TenantContext.require();
    }

    @PostMapping
    public ResponseEntity<Integration> createIntegration(@RequestBody CreateIntegrationRequest req) {
        return ResponseEntity.ok(integrationService.createIntegration(getTenantId(), req.getProvider(), req.getAuthType()));
    }

    @GetMapping
    public ResponseEntity<List<Integration>> getIntegrations() {
        return ResponseEntity.ok(integrationService.getIntegrations(getTenantId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Integration> getIntegration(@PathVariable UUID id) {
        return integrationService.getIntegration(id, getTenantId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/oauth/authorize")
    public ResponseEntity<String> authorizeOauth(@PathVariable UUID id) {
        return ResponseEntity.ok("https://oauth.provider.com/auth?client_id=123&state=" + id);
    }

    @PostMapping("/{id}/oauth/callback")
    public ResponseEntity<Void> oauthCallback(@PathVariable UUID id, @RequestBody OauthCallbackRequest req) {
        integrationService.saveOauthToken(id, getTenantId(), req.getCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/credentials")
    public ResponseEntity<Void> setCredentials(@PathVariable UUID id, @RequestBody CredentialsRequest req) {
        credentialService.saveCredentials(id, getTenantId(), null, null, req.getUsername(), req.getPassword());
        integrationService.updateStatus(id, getTenantId(), "HEALTHY");
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/test")
    public ResponseEntity<String> testConnection(@PathVariable UUID id) {
        // Mock test connection
        return ResponseEntity.ok("Connection successful");
    }

    @PostMapping("/{id}/sync-jobs")
    public ResponseEntity<SyncJob> createSyncJob(@PathVariable UUID id, @RequestBody SyncJobRequest req) {
        return ResponseEntity.ok(syncService.createSyncJob(id, getTenantId(), req.getScheduleCron(), req.getSourceEntity(), req.getTargetEntity()));
    }
    
    @GetMapping("/{id}/sync-jobs")
    public ResponseEntity<List<SyncJob>> getSyncJobs(@PathVariable UUID id) {
        return ResponseEntity.ok(syncService.getSyncJobs(id, getTenantId()));
    }

    @PostMapping("/sync-jobs/{jobId}/mappings")
    public ResponseEntity<FieldMapping> addFieldMapping(@PathVariable UUID jobId, @RequestBody FieldMappingRequest req) {
        return ResponseEntity.ok(syncService.addFieldMapping(jobId, getTenantId(), req.getSourceField(), req.getTargetField(), req.getTransformationRule()));
    }

    @PostMapping("/sync-jobs/{jobId}/run")
    public ResponseEntity<Void> runSyncJob(@PathVariable UUID jobId) {
        syncService.executeSyncJob(jobId, getTenantId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/audit-logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs(@PathVariable UUID id) {
        return ResponseEntity.ok(auditService.getLogs(id, getTenantId()));
    }
}
