package com.microsaas.creatoranalytics.controller;

import com.microsaas.creatoranalytics.model.ContentAsset;
import com.microsaas.creatoranalytics.service.ContentAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/creator-analytics/content-assets")
@RequiredArgsConstructor
public class ContentAssetController {
    private final ContentAssetService service;

    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000"); // Mock tenant ID for now
    }

    @GetMapping
    public List<ContentAsset> getAll() {
        return service.findAll(getTenantId());
    }

    @GetMapping("/{id}")
    public ContentAsset getById(@PathVariable UUID id) {
        return service.findById(id, getTenantId());
    }

    @PostMapping
    public ContentAsset create(@RequestBody ContentAsset entity) {
        return service.create(entity, getTenantId());
    }

    @PatchMapping("/{id}")
    public ContentAsset update(@PathVariable UUID id, @RequestBody ContentAsset entity) {
        return service.update(id, entity, getTenantId());
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.findById(id, getTenantId());
        return ResponseEntity.ok().build();
    }
}
