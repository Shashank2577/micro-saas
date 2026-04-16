package com.microsaas.equityintelligence.controllers;

import com.microsaas.equityintelligence.model.EquityGrant;
import com.microsaas.equityintelligence.model.VestingEvent;
import com.microsaas.equityintelligence.services.EquityGrantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EquityGrantController {

    private final EquityGrantService equityGrantService;

    @PostMapping("/grants")
    public ResponseEntity<EquityGrant> createGrant(
            @RequestBody EquityGrant grant,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(equityGrantService.createGrant(grant, tenantId));
    }

    @GetMapping("/grants/{stakeholderId}")
    public ResponseEntity<List<EquityGrant>> getGrantsByStakeholder(
            @PathVariable UUID stakeholderId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(equityGrantService.getGrantsByStakeholder(stakeholderId, tenantId));
    }

    @GetMapping("/vesting/schedule/{grantId}")
    public ResponseEntity<List<VestingEvent>> getVestingSchedule(
            @PathVariable UUID grantId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(equityGrantService.getVestingSchedule(grantId, tenantId));
    }
}
