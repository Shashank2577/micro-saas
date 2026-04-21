package com.micro_saas.featureflagai.controller;

import com.micro_saas.featureflagai.domain.FeatureFlag;
import com.micro_saas.featureflagai.service.FeatureFlagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flags")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    public FeatureFlagController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @PostMapping
    public ResponseEntity<FeatureFlag> createFlag(@RequestBody FeatureFlag flag) {
        FeatureFlag createdFlag = featureFlagService.createFlag(flag);
        return new ResponseEntity<>(createdFlag, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureFlag> getFlag(@PathVariable UUID id) {
        FeatureFlag flag = featureFlagService.getFlag(id);
        return ResponseEntity.ok(flag);
    }

    @GetMapping
    public ResponseEntity<List<FeatureFlag>> getAllFlags() {
        List<FeatureFlag> flags = featureFlagService.getAllFlags();
        return ResponseEntity.ok(flags);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlag> updateFlag(@PathVariable UUID id, @RequestBody FeatureFlag flagUpdates) {
        FeatureFlag updatedFlag = featureFlagService.updateFlag(id, flagUpdates);
        return ResponseEntity.ok(updatedFlag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlag(@PathVariable UUID id) {
        featureFlagService.deleteFlag(id);
        return ResponseEntity.noContent().build();
    }
}