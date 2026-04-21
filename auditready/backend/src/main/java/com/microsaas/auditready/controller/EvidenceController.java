package com.microsaas.auditready.controller;

import com.microsaas.auditready.model.Evidence;
import com.microsaas.auditready.service.AuditReadyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/evidence")
@RequiredArgsConstructor
public class EvidenceController {
    private final AuditReadyService service;

    @GetMapping
    public ResponseEntity<List<Evidence>> getEvidences() {
        return ResponseEntity.ok(service.getEvidences());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evidence> getEvidence(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getEvidence(id));
    }

    @PostMapping
    public ResponseEntity<Evidence> createEvidence(@RequestBody Evidence evidence) {
        return ResponseEntity.ok(service.createEvidence(evidence));
    }
}
