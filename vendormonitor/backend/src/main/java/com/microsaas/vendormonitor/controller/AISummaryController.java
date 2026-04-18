package com.microsaas.vendormonitor.controller;

import com.microsaas.vendormonitor.domain.RenewalSummary;
import com.microsaas.vendormonitor.service.AISummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AISummaryController {

    private final AISummaryService aiSummaryService;

    @Autowired
    public AISummaryController(AISummaryService aiSummaryService) {
        this.aiSummaryService = aiSummaryService;
    }

    @PostMapping("/vendors/{vendorId}/generate-summary")
    public ResponseEntity<RenewalSummary> generateSummary(@PathVariable UUID vendorId) {
        try {
            return ResponseEntity.ok(aiSummaryService.generateSummary(vendorId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/vendors/{vendorId}/summaries")
    public ResponseEntity<List<RenewalSummary>> getSummariesForVendor(@PathVariable UUID vendorId) {
        return ResponseEntity.ok(aiSummaryService.getSummariesForVendor(vendorId));
    }

    @GetMapping("/summaries/{id}")
    public ResponseEntity<RenewalSummary> getSummaryById(@PathVariable UUID id) {
        return aiSummaryService.getSummaryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
