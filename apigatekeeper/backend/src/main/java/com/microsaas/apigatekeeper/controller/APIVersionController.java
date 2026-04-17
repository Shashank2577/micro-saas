package com.microsaas.apigatekeeper.controller;

import com.microsaas.apigatekeeper.entity.APIVersion;
import com.microsaas.apigatekeeper.service.APIVersioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/versioning/api-versions")
@RequiredArgsConstructor
public class APIVersionController {

    private final APIVersioningService service;

    @PostMapping
    public ResponseEntity<APIVersion> createVersion(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody APIVersion version) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createVersion(tenantId, version));
    }

    @GetMapping
    public ResponseEntity<List<APIVersion>> getVersions(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.getVersions(tenantId));
    }
}
