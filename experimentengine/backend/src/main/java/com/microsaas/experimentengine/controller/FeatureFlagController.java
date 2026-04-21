package com.microsaas.experimentengine.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.experimentengine.domain.model.FeatureFlag;
import com.microsaas.experimentengine.domain.repository.FeatureFlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/feature-flags")
public class FeatureFlagController {

    private final FeatureFlagRepository featureFlagRepository;

    @Autowired
    public FeatureFlagController(FeatureFlagRepository featureFlagRepository) {
        this.featureFlagRepository = featureFlagRepository;
    }

    @GetMapping
    public ResponseEntity<List<FeatureFlag>> getAllFeatureFlags() {
        return ResponseEntity.ok(featureFlagRepository.findByTenantId(TenantContext.require()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureFlag> getFeatureFlagById(@PathVariable UUID id) {
        return featureFlagRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FeatureFlag> createFeatureFlag(@RequestBody FeatureFlag featureFlag) {
        featureFlag.setTenantId(TenantContext.require());
        return ResponseEntity.ok(featureFlagRepository.save(featureFlag));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlag> updateFeatureFlag(@PathVariable UUID id, @RequestBody FeatureFlag featureFlag) {
        return featureFlagRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(existing -> {
                    featureFlag.setId(existing.getId());
                    featureFlag.setTenantId(existing.getTenantId());
                    return ResponseEntity.ok(featureFlagRepository.save(featureFlag));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<FeatureFlag> toggleFeatureFlag(@PathVariable UUID id) {
        return featureFlagRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(existing -> {
                    existing.setEnabled(!existing.isEnabled());
                    return ResponseEntity.ok(featureFlagRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeatureFlag(@PathVariable UUID id) {
        return featureFlagRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(existing -> {
                    featureFlagRepository.delete(existing);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
