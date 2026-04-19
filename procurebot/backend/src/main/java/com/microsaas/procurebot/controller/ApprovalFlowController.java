package com.microsaas.procurebot.controller;

import com.microsaas.procurebot.dto.ApprovalFlowRequest;
import com.microsaas.procurebot.model.ApprovalFlow;
import com.microsaas.procurebot.service.ApprovalFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/procurement/approval-flows")
@RequiredArgsConstructor
public class ApprovalFlowController {

    private final ApprovalFlowService service;

    // TODO: Fetch tenantId from authentication context, currently hardcoded or passed as header for MVP
    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    @GetMapping
    public ResponseEntity<List<ApprovalFlow>> getAll() {
        return ResponseEntity.ok(service.findAll(getTenantId()));
    }

    @PostMapping
    public ResponseEntity<ApprovalFlow> create(@RequestBody ApprovalFlowRequest request) {
        return ResponseEntity.ok(service.create(getTenantId(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprovalFlow> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id, getTenantId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApprovalFlow> update(@PathVariable UUID id, @RequestBody ApprovalFlowRequest request) {
        return ResponseEntity.ok(service.update(id, getTenantId(), request));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {
        service.validate(id, getTenantId());
        return ResponseEntity.ok().build();
    }
}
