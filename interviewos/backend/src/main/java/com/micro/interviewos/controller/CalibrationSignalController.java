package com.micro.interviewos.controller;

import com.micro.interviewos.dto.CalibrationSignalDTO;
import com.micro.interviewos.service.CalibrationSignalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews/calibration-signals")
@RequiredArgsConstructor
public class CalibrationSignalController {

    private final CalibrationSignalService service;

    @GetMapping
    public ResponseEntity<List<CalibrationSignalDTO>> findAll(@RequestHeader("X-Tenant-Id") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @PostMapping
    public ResponseEntity<CalibrationSignalDTO> create(@RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody CalibrationSignalDTO dto) {
        dto.setTenantId(tenantId);
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalibrationSignalDTO> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        return service.findById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CalibrationSignalDTO> update(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody CalibrationSignalDTO dto) {
        return ResponseEntity.ok(service.update(id, tenantId, dto));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        service.validate(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
