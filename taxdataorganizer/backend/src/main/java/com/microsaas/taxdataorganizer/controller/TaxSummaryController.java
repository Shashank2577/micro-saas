package com.microsaas.taxdataorganizer.controller;

import com.microsaas.taxdataorganizer.model.TaxSummary;
import com.microsaas.taxdataorganizer.service.TaxSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tax-summaries")
@RequiredArgsConstructor
public class TaxSummaryController {

    private final TaxSummaryService taxSummaryService;

    @PostMapping("/generate")
    public ResponseEntity<TaxSummary> generateSummary(@RequestParam UUID taxYearId, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(taxSummaryService.generateSummary(taxYearId, tenantId));
    }
}
