package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.dto.BrandProfileRequest;
import com.microsaas.brandvoice.entity.BrandProfile;
import com.microsaas.brandvoice.service.BrandProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/brand-profiles")
@RequiredArgsConstructor
public class BrandProfileController {

    private final BrandProfileService service;

    @PostMapping
    public ResponseEntity<BrandProfile> createProfile(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody BrandProfileRequest request) {
        return ResponseEntity.ok(service.createProfile(tenantId, request));
    }

    @GetMapping
    public ResponseEntity<List<BrandProfile>> getProfiles(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.getProfiles(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandProfile> getProfile(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.getProfile(tenantId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandProfile> updateProfile(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id,
            @RequestBody BrandProfileRequest request) {
        return ResponseEntity.ok(service.updateProfile(tenantId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        service.deleteProfile(tenantId, id);
        return ResponseEntity.noContent().build();
    }
}
