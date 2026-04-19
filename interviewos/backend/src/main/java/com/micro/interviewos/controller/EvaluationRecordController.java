package com.micro.interviewos.controller;

import com.micro.interviewos.dto.EvaluationRecordDTO;
import com.micro.interviewos.service.EvaluationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews/evaluation-records")
@RequiredArgsConstructor
public class EvaluationRecordController {

    private final EvaluationRecordService service;

    @GetMapping
    public ResponseEntity<List<EvaluationRecordDTO>> findAll(@RequestHeader("X-Tenant-Id") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @PostMapping
    public ResponseEntity<EvaluationRecordDTO> create(@RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody EvaluationRecordDTO dto) {
        dto.setTenantId(tenantId);
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationRecordDTO> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        return service.findById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EvaluationRecordDTO> update(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody EvaluationRecordDTO dto) {
        return ResponseEntity.ok(service.update(id, tenantId, dto));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        service.validate(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
