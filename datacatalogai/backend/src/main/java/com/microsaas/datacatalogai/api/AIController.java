package com.microsaas.datacatalogai.api;

import com.microsaas.datacatalogai.domain.model.Asset;
import com.microsaas.datacatalogai.domain.model.AssetColumn;
import com.microsaas.datacatalogai.service.AssetService;
import com.microsaas.datacatalogai.service.PIIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Tag(name = "AI Operations", description = "Endpoints for AI document generation and PII detection")
public class AIController {

    private final AssetService assetService;
    private final PIIService piiService;

    @PostMapping("/document-asset")
    @Operation(summary = "Auto-document an asset using AI")
    public Asset documentAsset(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Map<String, String> body) {
        UUID assetId = UUID.fromString(body.get("assetId"));
        return assetService.documentAssetViaAI(tenantId, assetId);
    }

    @PostMapping("/detect-pii")
    @Operation(summary = "Detect PII in an asset using AI")
    public List<AssetColumn> detectPii(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Map<String, String> body) {
        UUID assetId = UUID.fromString(body.get("assetId"));
        return piiService.detectPiiForAsset(tenantId, assetId);
    }
}
