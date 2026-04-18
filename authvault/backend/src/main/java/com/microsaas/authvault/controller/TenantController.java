package com.microsaas.authvault.controller;

import com.microsaas.authvault.entity.Tenant;
import com.microsaas.authvault.repository.AuthVaultTenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final AuthVaultTenantRepository tenantRepository;

    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
        return ResponseEntity.ok(tenantRepository.save(tenant));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenant(@PathVariable UUID id) {
        return tenantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
