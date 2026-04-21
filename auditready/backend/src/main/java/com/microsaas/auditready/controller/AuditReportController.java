package com.microsaas.auditready.controller;

import com.microsaas.auditready.model.AuditReport;
import com.microsaas.auditready.service.AuditReadyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class AuditReportController {
    private final AuditReadyService service;

    @GetMapping
    public ResponseEntity<List<AuditReport>> getReports() {
        return ResponseEntity.ok(service.getReports());
    }

    @PostMapping
    public ResponseEntity<AuditReport> createReport(@RequestBody AuditReport report) {
        return ResponseEntity.ok(service.createReport(report));
    }
}
