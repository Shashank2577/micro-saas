package com.microsaas.apigatekeeper.controller;

import com.microsaas.apigatekeeper.entity.RateLimitPolicy;
import com.microsaas.apigatekeeper.service.RateLimitPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ratelimit/policies")
@RequiredArgsConstructor
public class RateLimitPolicyController {

    private final RateLimitPolicyService service;

    @PostMapping
    public ResponseEntity<RateLimitPolicy> createPolicy(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody RateLimitPolicy policy) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createPolicy(tenantId, policy));
    }

    @GetMapping
    public ResponseEntity<List<RateLimitPolicy>> getPolicies(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.getPolicies(tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID id) {
        try {
            service.deletePolicy(tenantId, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
