package com.microsaas.creatoranalytics.controller;

import com.microsaas.creatoranalytics.model.AudienceSegment;
import com.microsaas.creatoranalytics.service.AudienceSegmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/creator-analytics/audience-segments")
@RequiredArgsConstructor
public class AudienceSegmentController {
    private final AudienceSegmentService service;

    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000"); // Mock tenant ID for now
    }

    @GetMapping
    public List<AudienceSegment> getAll() {
        return service.findAll(getTenantId());
    }

    @GetMapping("/{id}")
    public AudienceSegment getById(@PathVariable UUID id) {
        return service.findById(id, getTenantId());
    }

    @PostMapping
    public AudienceSegment create(@RequestBody AudienceSegment entity) {
        return service.create(entity, getTenantId());
    }

    @PatchMapping("/{id}")
    public AudienceSegment update(@PathVariable UUID id, @RequestBody AudienceSegment entity) {
        return service.update(id, entity, getTenantId());
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.findById(id, getTenantId());
        return ResponseEntity.ok().build();
    }
}
