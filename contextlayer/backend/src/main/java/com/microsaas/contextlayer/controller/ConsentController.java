package com.microsaas.contextlayer.controller;

import com.microsaas.contextlayer.domain.ConsentRecord;
import com.microsaas.contextlayer.dto.ConsentRecordDTO;
import com.microsaas.contextlayer.service.PrivacyEnforcementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consent")
@RequiredArgsConstructor
public class ConsentController {

    private final PrivacyEnforcementService privacyEnforcementService;

    @GetMapping("/{customerId}")
    public ResponseEntity<ConsentRecord> getConsent(
            @PathVariable String customerId,
            @RequestParam(required = false, defaultValue = "data_processing") String type) {
        return privacyEnforcementService.getConsent(customerId, type)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ConsentRecord> recordConsent(
            @RequestBody ConsentRecordDTO dto,
            @RequestHeader(value = "X-App-Id", defaultValue = "unknown") String appId) {
        return ResponseEntity.ok(privacyEnforcementService.recordConsent(dto.getCustomerId(), dto.getConsentType(), dto.getGranted(), appId));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> revokeConsent(
            @PathVariable String customerId) {
        privacyEnforcementService.revokeConsent(customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{customerId}/history")
    public ResponseEntity<List<ConsentRecord>> getConsentHistory(
            @PathVariable String customerId) {
        return ResponseEntity.ok(privacyEnforcementService.getConsentHistory(customerId));
    }
}
