package com.microsaas.healthcaredocai.controller;

import com.microsaas.healthcaredocai.service.EHRIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/ehr-integrations")
@RequiredArgsConstructor
public class EHRIntegrationController {

    private final EHRIntegrationService ehrIntegrationService;

    @PostMapping("/sync")
    public ResponseEntity<Boolean> syncToEHR(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID noteId,
            @RequestParam String ehrSystem) {
        boolean result = ehrIntegrationService.syncToEHR(tenantId, noteId, ehrSystem);
        return ResponseEntity.ok(result);
    }
}
