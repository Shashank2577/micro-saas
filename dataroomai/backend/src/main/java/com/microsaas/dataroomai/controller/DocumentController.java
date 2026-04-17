package com.microsaas.dataroomai.controller;

import com.microsaas.dataroomai.domain.Document;
import com.microsaas.dataroomai.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/data-rooms/{roomId}/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<Document>> getDocuments(
            @PathVariable UUID roomId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(documentService.getDocuments(roomId, tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(documentService.getDocument(id, tenantId));
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(
            @RequestBody Document document,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        document.setTenantId(tenantId);
        return ResponseEntity.ok(documentService.createDocument(document));
    }
}
