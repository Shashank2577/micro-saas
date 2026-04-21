package com.microsaas.seointelligence.controller;

import com.microsaas.seointelligence.dto.SeoAuditRequest;
import com.microsaas.seointelligence.model.SeoAudit;
import com.microsaas.seointelligence.service.SeoAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seo-audits")
@RequiredArgsConstructor
public class SeoAuditController {

    private final SeoAuditService seoAuditService;

    @GetMapping
    public ResponseEntity<List<SeoAudit>> listAudits() {
        return ResponseEntity.ok(seoAuditService.listAudits());
    }

    @PostMapping
    public ResponseEntity<SeoAudit> createAudit(@RequestBody SeoAuditRequest request) {
        return ResponseEntity.ok(seoAuditService.createAudit(request));
    }
}
