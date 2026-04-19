package com.micro.interviewos.controller;

import com.micro.interviewos.dto.ScorecardDTO;
import com.micro.interviewos.service.ScorecardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews/scorecards")
@RequiredArgsConstructor
public class ScorecardController {

    private final ScorecardService service;

    @GetMapping
    public ResponseEntity<List<ScorecardDTO>> findAll(@RequestHeader("X-Tenant-Id") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @PostMapping
    public ResponseEntity<ScorecardDTO> create(@RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody ScorecardDTO dto) {
        dto.setTenantId(tenantId);
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScorecardDTO> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        return service.findById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScorecardDTO> update(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody ScorecardDTO dto) {
        return ResponseEntity.ok(service.update(id, tenantId, dto));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        service.validate(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
