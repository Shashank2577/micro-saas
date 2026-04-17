package com.microsaas.apigatekeeper.controller;

import com.microsaas.apigatekeeper.entity.CachePolicy;
import com.microsaas.apigatekeeper.repository.CachePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
public class CachePolicyController {

    private final CachePolicyRepository repository;

    @PostMapping("/policies")
    public ResponseEntity<CachePolicy> createPolicy(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody CachePolicy policy) {
        policy.setTenantId(tenantId);
        policy.setCreatedAt(ZonedDateTime.now());
        policy.setUpdatedAt(ZonedDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(policy));
    }

    @GetMapping("/policies")
    public ResponseEntity<List<CachePolicy>> getPolicies(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(repository.findByTenantId(tenantId));
    }
}
