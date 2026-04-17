package com.microsaas.dataroomai.controller;

import com.microsaas.dataroomai.domain.DiligenceGap;
import com.microsaas.dataroomai.service.DiligenceGapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/data-rooms/{roomId}/gaps")
@RequiredArgsConstructor
public class DiligenceGapController {

    private final DiligenceGapService diligenceGapService;

    @GetMapping
    public ResponseEntity<List<DiligenceGap>> getGaps(
            @PathVariable UUID roomId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(diligenceGapService.getDiligenceGaps(roomId, tenantId));
    }

    @PostMapping
    public ResponseEntity<DiligenceGap> createGap(
            @RequestBody DiligenceGap gap,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        gap.setTenantId(tenantId);
        return ResponseEntity.ok(diligenceGapService.createDiligenceGap(gap));
    }
}
