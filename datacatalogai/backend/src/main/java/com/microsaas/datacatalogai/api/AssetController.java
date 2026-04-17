package com.microsaas.datacatalogai.api;

import com.microsaas.datacatalogai.domain.model.Asset;
import com.microsaas.datacatalogai.domain.model.AssetColumn;
import com.microsaas.datacatalogai.domain.model.QueryLog;
import com.microsaas.datacatalogai.domain.model.Relationship;
import com.microsaas.datacatalogai.service.AssetService;
import com.microsaas.datacatalogai.service.QueryLogService;
import com.microsaas.datacatalogai.service.RelationshipService;
import com.microsaas.datacatalogai.service.TrustService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
@Tag(name = "Assets", description = "Endpoints for managing data assets")
public class AssetController {

    private final AssetService assetService;
    private final QueryLogService queryLogService;
    private final RelationshipService relationshipService;
    private final TrustService trustService;

    @GetMapping
    @Operation(summary = "List assets")
    public Page<Asset> listAssets(@RequestHeader("X-Tenant-ID") String tenantId, Pageable pageable) {
        return assetService.listAssets(tenantId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get asset detail")
    public Asset getAsset(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return assetService.getAsset(tenantId, id);
    }

    @GetMapping("/search")
    @Operation(summary = "Semantic search for assets")
    public List<Asset> searchAssets(@RequestHeader("X-Tenant-ID") String tenantId, @RequestParam String q) {
        return assetService.searchAssets(tenantId, q);
    }

    @PutMapping("/{id}/description")
    @Operation(summary = "Update asset description")
    public Asset updateDescription(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody Map<String, String> body) {
        return assetService.updateDescription(tenantId, id, body.get("description"));
    }

    @PutMapping("/{id}/owner")
    @Operation(summary = "Update asset owner")
    public Asset updateOwner(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody Map<String, String> body) {
        return assetService.updateOwner(tenantId, id, body.get("ownerId"));
    }

    @PutMapping("/{id}/tags")
    @Operation(summary = "Update asset tags")
    public Asset updateTags(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody List<String> tags) {
        return assetService.updateTags(tenantId, id, tags);
    }

    @GetMapping("/{id}/columns")
    @Operation(summary = "Get columns for an asset")
    public List<AssetColumn> getColumns(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return assetService.getAssetColumns(tenantId, id);
    }

    @GetMapping("/{id}/lineage")
    @Operation(summary = "Get lineage for an asset")
    public List<Relationship> getLineage(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return relationshipService.getLineage(tenantId, id);
    }

    @GetMapping("/{id}/popularity")
    @Operation(summary = "Get asset popularity (queries count as proxy)")
    public Map<String, Object> getPopularity(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        List<QueryLog> logs = queryLogService.getQueries(tenantId, id);
        return Map.of("queryCount", logs.size(), "assetId", id);
    }

    @GetMapping("/{id}/queries")
    @Operation(summary = "Get query logs for an asset")
    public List<QueryLog> getQueries(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        return queryLogService.getQueries(tenantId, id);
    }
}
