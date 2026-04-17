package com.microsaas.datalineagetracker.controller;

import com.microsaas.datalineagetracker.dto.LineageLinkDto;
import com.microsaas.datalineagetracker.entity.DataLineageLink;
import com.microsaas.datalineagetracker.service.LineageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lineage")
@RequiredArgsConstructor
@Tag(name = "Data Lineage", description = "Endpoints for lineage tracking")
public class LineageController {
    private final LineageService service;

    @GetMapping
    @Operation(summary = "Get all lineage links")
    public List<DataLineageLink> getAllLinks() {
        return service.getAllLinks();
    }

    @PostMapping("/links")
    @Operation(summary = "Create a lineage link between assets asynchronously")
    public ResponseEntity<String> createLinkAsync(@RequestBody LineageLinkDto dto) {
        service.createLinkAsync(dto);
        return ResponseEntity.accepted().body("Lineage computation submitted successfully.");
    }

    @GetMapping("/upstream/{assetId}")
    @Operation(summary = "Get upstream lineage for an asset")
    public List<DataLineageLink> getUpstream(@PathVariable UUID assetId) {
        return service.getUpstream(assetId);
    }

    @GetMapping("/downstream/{assetId}")
    @Operation(summary = "Get downstream lineage for an asset")
    public List<DataLineageLink> getDownstream(@PathVariable UUID assetId) {
        return service.getDownstream(assetId);
    }
}
