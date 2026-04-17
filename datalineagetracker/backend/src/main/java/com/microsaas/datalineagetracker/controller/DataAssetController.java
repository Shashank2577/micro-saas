package com.microsaas.datalineagetracker.controller;

import com.microsaas.datalineagetracker.dto.AssetDto;
import com.microsaas.datalineagetracker.entity.DataAsset;
import com.microsaas.datalineagetracker.entity.PiiTag;
import com.microsaas.datalineagetracker.service.DataAssetService;
import com.microsaas.datalineagetracker.service.PiiScannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
@Tag(name = "Data Assets", description = "Endpoints for managing data assets")
public class DataAssetController {
    private final DataAssetService service;
    private final PiiScannerService piiService;

    @GetMapping
    @Operation(summary = "List all data assets")
    public List<DataAsset> getAllAssets() {
        return service.getAllAssets();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get asset by ID")
    public DataAsset getAssetById(@PathVariable UUID id) {
        return service.getAssetById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new data asset")
    public DataAsset createAsset(@RequestBody AssetDto dto) {
        return service.createAsset(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing data asset")
    public DataAsset updateAsset(@PathVariable UUID id, @RequestBody AssetDto dto) {
        return service.updateAsset(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an asset")
    public ResponseEntity<Void> deleteAsset(@PathVariable UUID id) {
        service.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/scan-pii")
    @Operation(summary = "Scan asset for PII tags using LLM")
    public List<PiiTag> scanAssetPii(@PathVariable UUID id) {
        return piiService.scanAsset(id);
    }
    
    @GetMapping("/{id}/pii-tags")
    @Operation(summary = "Get PII tags for an asset")
    public List<PiiTag> getAssetTags(@PathVariable UUID id) {
        return piiService.getAssetTags(id);
    }
}
