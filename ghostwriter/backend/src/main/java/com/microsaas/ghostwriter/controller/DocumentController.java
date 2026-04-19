package com.microsaas.ghostwriter.controller;

import com.microsaas.ghostwriter.domain.Document;
import com.microsaas.ghostwriter.dto.DocumentUpdateRequest;
import com.microsaas.ghostwriter.dto.GenerateRequest;
import com.microsaas.ghostwriter.service.DocumentService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments(@RequestParam(required = false) String format) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(documentService.getAllDocuments(tenantId, format));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(documentService.getDocumentById(id, tenantId));
    }

    @PostMapping("/generate")
    public ResponseEntity<Document> generateDocument(@RequestBody GenerateRequest request) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.generateDocument(tenantId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable UUID id, @RequestBody DocumentUpdateRequest request) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(documentService.updateDocument(id, tenantId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        documentService.deleteDocument(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
