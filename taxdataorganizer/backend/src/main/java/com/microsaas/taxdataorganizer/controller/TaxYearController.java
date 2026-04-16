package com.microsaas.taxdataorganizer.controller;

import com.microsaas.taxdataorganizer.model.TaxYear;
import com.microsaas.taxdataorganizer.service.TaxYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tax-years")
@RequiredArgsConstructor
public class TaxYearController {

    private final TaxYearService taxYearService;

    @PostMapping
    public ResponseEntity<TaxYear> createTaxYear(@RequestParam int year, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(taxYearService.createTaxYear(year, tenantId));
    }

    @PutMapping("/{id}/ready")
    public ResponseEntity<TaxYear> markReadyForAccountant(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(taxYearService.markReadyForAccountant(id, tenantId));
    }

    @GetMapping
    public ResponseEntity<List<TaxYear>> listYears(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(taxYearService.listYears(tenantId));
    }
}
