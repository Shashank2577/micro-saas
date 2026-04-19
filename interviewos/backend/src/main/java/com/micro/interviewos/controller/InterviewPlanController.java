package com.micro.interviewos.controller;

import com.micro.interviewos.dto.InterviewPlanDTO;
import com.micro.interviewos.service.InterviewPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews/interview-plans")
@RequiredArgsConstructor
public class InterviewPlanController {

    private final InterviewPlanService service;

    @GetMapping
    public ResponseEntity<List<InterviewPlanDTO>> findAll(@RequestHeader("X-Tenant-Id") UUID tenantId) {
        return ResponseEntity.ok(service.findAll(tenantId));
    }

    @PostMapping
    public ResponseEntity<InterviewPlanDTO> create(@RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody InterviewPlanDTO dto) {
        dto.setTenantId(tenantId);
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewPlanDTO> findById(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        return service.findById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InterviewPlanDTO> update(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody InterviewPlanDTO dto) {
        return ResponseEntity.ok(service.update(id, tenantId, dto));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        service.validate(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
