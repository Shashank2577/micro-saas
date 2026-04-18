package com.microsaas.insightengine.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.insightengine.entity.CustomDiscoveryRule;
import com.microsaas.insightengine.repository.CustomDiscoveryRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/discovery/rules")
@RequiredArgsConstructor
public class CustomDiscoveryRuleController {

    private final CustomDiscoveryRuleRepository customDiscoveryRuleRepository;

    @GetMapping
    public List<CustomDiscoveryRule> getRules() {
        UUID tenantId = TenantContext.require();
        return customDiscoveryRuleRepository.findByTenantId(tenantId);
    }

    @PostMapping
    public CustomDiscoveryRule createRule(@RequestBody CustomDiscoveryRule rule) {
        UUID tenantId = TenantContext.require();
        rule.setTenantId(tenantId);
        return customDiscoveryRuleRepository.save(rule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        if (customDiscoveryRuleRepository.findByIdAndTenantId(id, tenantId).isPresent()) {
            customDiscoveryRuleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
