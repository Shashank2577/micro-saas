package com.microsaas.equityintelligence.controllers;

import com.microsaas.equityintelligence.model.Stakeholder;
import com.microsaas.equityintelligence.services.StakeholderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stakeholders")
@RequiredArgsConstructor
public class StakeholderController {

    private final StakeholderService stakeholderService;

    @PostMapping
    public ResponseEntity<Stakeholder> createStakeholder(
            @RequestBody Stakeholder stakeholder,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(stakeholderService.createStakeholder(stakeholder, tenantId));
    }

    @GetMapping
    public ResponseEntity<List<Stakeholder>> getStakeholders(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(stakeholderService.getStakeholders(tenantId));
    }
}
