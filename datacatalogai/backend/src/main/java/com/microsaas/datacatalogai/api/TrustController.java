package com.microsaas.datacatalogai.api;

import com.microsaas.datacatalogai.service.TrustService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trust")
@RequiredArgsConstructor
@Tag(name = "Trust", description = "Endpoints for checking asset trust score")
public class TrustController {

    private final TrustService trustService;

    @GetMapping("/{assetId}")
    @Operation(summary = "Get trust breakdown for an asset")
    public Map<String, Object> getTrustBreakdown(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID assetId) {
        return trustService.getTrustBreakdown(tenantId, assetId);
    }
}
