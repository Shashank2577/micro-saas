package com.microsaas.taxdataorganizer.controller;

import com.microsaas.taxdataorganizer.model.DocumentStatus;
import com.microsaas.taxdataorganizer.model.DocumentType;
import com.microsaas.taxdataorganizer.model.TaxDocument;
import com.microsaas.taxdataorganizer.service.TaxDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tax-documents")
@RequiredArgsConstructor
public class TaxDocumentController {

    private final TaxDocumentService taxDocumentService;

    @PostMapping
    public ResponseEntity<TaxDocument> uploadDocument(
            @RequestParam UUID taxYearId,
            @RequestParam String fileName,
            @RequestParam DocumentType documentType,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(taxDocumentService.uploadDocument(taxYearId, fileName, documentType, tenantId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaxDocument> updateDocumentStatus(
            @PathVariable UUID id,
            @RequestParam DocumentStatus status,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(taxDocumentService.updateDocumentStatus(id, status, tenantId));
    }

    @GetMapping("/tax-years/{taxYearId}")
    public ResponseEntity<List<TaxDocument>> listDocumentsByTaxYear(
            @PathVariable UUID taxYearId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(taxDocumentService.listDocumentsByTaxYear(taxYearId, tenantId));
    }
}
