package com.micro.interviewos.controller;

import com.micro.interviewos.dto.DecisionPacketDTO;
import com.micro.interviewos.service.DecisionPacketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews/decision-packets")
@RequiredArgsConstructor
public class DecisionPacketController {

    private final DecisionPacketService service;

    @GetMapping
    public ResponseEntity<List<DecisionPacketDTO>> findAll(@RequestHeader("X-Tenant-Id") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @PostMapping
    public ResponseEntity<DecisionPacketDTO> create(@RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody DecisionPacketDTO dto) {
        dto.setTenantId(tenantId);
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DecisionPacketDTO> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        return service.findById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DecisionPacketDTO> update(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody DecisionPacketDTO dto) {
        return ResponseEntity.ok(service.update(id, tenantId, dto));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        service.validate(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
