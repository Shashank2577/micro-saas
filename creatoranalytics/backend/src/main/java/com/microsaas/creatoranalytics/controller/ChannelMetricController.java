package com.microsaas.creatoranalytics.controller;

import com.microsaas.creatoranalytics.model.ChannelMetric;
import com.microsaas.creatoranalytics.service.ChannelMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/creator-analytics/channel-metrics")
@RequiredArgsConstructor
public class ChannelMetricController {
    private final ChannelMetricService service;

    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000"); // Mock tenant ID for now
    }

    @GetMapping
    public List<ChannelMetric> getAll() {
        return service.findAll(getTenantId());
    }

    @GetMapping("/{id}")
    public ChannelMetric getById(@PathVariable UUID id) {
        return service.findById(id, getTenantId());
    }

    @PostMapping
    public ChannelMetric create(@RequestBody ChannelMetric entity) {
        return service.create(entity, getTenantId());
    }

    @PatchMapping("/{id}")
    public ChannelMetric update(@PathVariable UUID id, @RequestBody ChannelMetric entity) {
        return service.update(id, entity, getTenantId());
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.findById(id, getTenantId());
        return ResponseEntity.ok().build();
    }
}
