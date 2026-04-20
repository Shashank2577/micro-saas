package com.microsaas.contextlayer.controller;

import com.microsaas.contextlayer.domain.ConsentRecord;
import com.microsaas.contextlayer.domain.ContextVersion;
import com.microsaas.contextlayer.domain.CustomerContext;
import com.microsaas.contextlayer.domain.CustomerPreference;
import com.microsaas.contextlayer.dto.ConsentRecordDTO;
import com.microsaas.contextlayer.dto.ContextUpdateDTO;
import com.microsaas.contextlayer.service.ContextRetrievalService;
import com.microsaas.contextlayer.service.ContextUpdateService;
import com.microsaas.contextlayer.service.PreferenceLearningService;
import com.microsaas.contextlayer.service.PrivacyEnforcementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers/{customerId}")
@RequiredArgsConstructor
public class ContextController {
    private final ContextRetrievalService retrievalService;
    private final ContextUpdateService updateService;
    private final PreferenceLearningService preferenceService;
    private final PrivacyEnforcementService privacyService;

    @GetMapping("/context")
    public ResponseEntity<CustomerContext> getContext(@PathVariable String customerId) {
        return retrievalService.getContext(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/context")
    public ResponseEntity<CustomerContext> updateContext(
            @PathVariable String customerId,
            @RequestBody ContextUpdateDTO updateDTO,
            @RequestHeader(value = "X-App-Id", defaultValue = "unknown") String appId) {
        return ResponseEntity.ok(updateService.updateContext(customerId, updateDTO, appId));
    }

    @GetMapping("/context/{attribute}")
    public ResponseEntity<String> getContextAttribute(@PathVariable String customerId, @PathVariable String attribute) {
        return retrievalService.getContext(customerId)
                .map(ctx -> ResponseEntity.ok(ctx.getAttributes()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/context/{attribute}")
    public ResponseEntity<CustomerContext> updateContextAttribute(
            @PathVariable String customerId,
            @PathVariable String attribute,
            @RequestBody com.microsaas.contextlayer.dto.ContextAttributeDTO dto,
            @RequestHeader(value = "X-App-Id", defaultValue = "unknown") String appId) {
        return ResponseEntity.ok(updateService.updateAttribute(customerId, attribute, dto.getValue(), appId));
    }

    @PostMapping("/context/version")
    public ResponseEntity<Void> createContextSnapshot(
            @PathVariable String customerId,
            @RequestHeader(value = "X-App-Id", defaultValue = "unknown") String appId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/audit-log")
    public ResponseEntity<List<ContextVersion>> getAuditLog(@PathVariable String customerId) {
        return ResponseEntity.ok(updateService.getContextVersions(customerId));
    }

    @PostMapping("/context/export")
    public ResponseEntity<Void> exportContext(@PathVariable String customerId) {
        updateService.exportContext(customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/context/versions")
    public ResponseEntity<List<ContextVersion>> getContextVersions(@PathVariable String customerId) {
        return ResponseEntity.ok(updateService.getContextVersions(customerId));
    }

    @PostMapping("/context/rollback")
    public ResponseEntity<Void> rollbackContext(@PathVariable String customerId, @RequestParam UUID versionId) {
        updateService.rollbackContext(customerId, versionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/context")
    public ResponseEntity<Void> deleteContext(@PathVariable String customerId) {
        updateService.deleteContext(customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/preferences")
    public ResponseEntity<List<CustomerPreference>> getPreferences(@PathVariable String customerId) {
        return ResponseEntity.ok(preferenceService.getPreferences(customerId));
    }

    @PutMapping("/preferences/{key}")
    public ResponseEntity<CustomerPreference> updatePreference(
            @PathVariable String customerId,
            @PathVariable String key,
            @RequestBody String value,
            @RequestHeader(value = "X-App-Id", defaultValue = "unknown") String appId) {
        return ResponseEntity.ok(preferenceService.savePreference(customerId, key, value, appId));
    }

    @PostMapping("/consent")
    public ResponseEntity<ConsentRecord> recordConsent(
            @PathVariable String customerId,
            @RequestBody ConsentRecordDTO dto,
            @RequestHeader(value = "X-App-Id", defaultValue = "unknown") String appId) {
        return ResponseEntity.ok(privacyService.recordConsent(customerId, dto.getConsentType(), dto.getGranted(), appId));
    }
}
