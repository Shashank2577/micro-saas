package com.microsaas.datacatalogai.api;

import com.microsaas.datacatalogai.domain.model.AssetColumn;
import com.microsaas.datacatalogai.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/columns")
@RequiredArgsConstructor
@Tag(name = "Columns", description = "Endpoints for managing asset columns")
public class ColumnController {

    private final AssetService assetService;

    @PutMapping("/{id}/description")
    @Operation(summary = "Update column description")
    public AssetColumn updateColumnDescription(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody Map<String, String> body) {
        return assetService.updateColumnDescription(tenantId, id, body.get("description"));
    }
}
