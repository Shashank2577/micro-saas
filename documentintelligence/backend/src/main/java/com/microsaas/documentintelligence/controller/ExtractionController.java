package com.microsaas.documentintelligence.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.documentintelligence.service.ExtractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/extractions")
@RequiredArgsConstructor
@Tag(name = "Extractions", description = "Document Extraction API")
public class ExtractionController {

    private final ExtractionService extractionService;

    @PostMapping("/{documentId}/process")
    @Operation(summary = "Process a document for extraction")
    public ResponseEntity<Void> processDocument(@PathVariable UUID documentId) {
        UUID tenantId = TenantContext.require();
        extractionService.processDocument(documentId, tenantId);
        return ResponseEntity.accepted().build();
    }
}
