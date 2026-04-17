package com.microsaas.taxoptimizer.controller;

import com.microsaas.taxoptimizer.domain.entity.TaxProfile;
import com.microsaas.taxoptimizer.service.TaxProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class TaxProfileController {

    private final TaxProfileService taxProfileService;

    @PostMapping
    public ResponseEntity<TaxProfile> createOrUpdateProfile(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId,
            @RequestBody TaxProfile profileData) {
        return ResponseEntity.ok(taxProfileService.createOrUpdateProfile(tenantId, userId, profileData));
    }

    @GetMapping
    public ResponseEntity<TaxProfile> getProfile(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId) {
        return taxProfileService.getProfile(tenantId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
