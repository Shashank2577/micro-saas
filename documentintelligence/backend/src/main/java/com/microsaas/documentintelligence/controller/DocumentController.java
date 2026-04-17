package com.microsaas.documentintelligence.controller;

import com.microsaas.documentintelligence.dto.DocumentDTO;
import com.microsaas.documentintelligence.dto.DocumentExtractionDTO;
import com.microsaas.documentintelligence.dto.QAQuery;
import com.microsaas.documentintelligence.dto.QAResponse;
import com.microsaas.documentintelligence.dto.SearchQuery;
import com.microsaas.documentintelligence.model.DocumentChunk;
import com.microsaas.documentintelligence.service.DocumentService;
import com.microsaas.documentintelligence.service.QAService;
import com.microsaas.documentintelligence.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "Document Processing API")
public class DocumentController {

    private final DocumentService documentService;
    private final QAService qaService;
    private final SearchService searchService;

    @PostMapping("/upload")
    @Operation(summary = "Upload a document")
    public ResponseEntity<DocumentDTO> uploadDocument(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(documentService.uploadDocument(file));
    }

    @GetMapping
    @Operation(summary = "Get all documents")
    public ResponseEntity<Page<DocumentDTO>> getDocuments(Pageable pageable) {
        return ResponseEntity.ok(documentService.getDocuments(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document by ID")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.getDocument(id));
    }

    @GetMapping("/{id}/extractions")
    @Operation(summary = "Get document extractions")
    public ResponseEntity<List<DocumentExtractionDTO>> getExtractions(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.getExtractions(id));
    }

    @PostMapping("/search")
    @Operation(summary = "Semantic search across documents")
    public ResponseEntity<List<DocumentChunk>> search(@RequestBody SearchQuery query) {
        return ResponseEntity.ok(searchService.search(query));
    }

    @PostMapping("/{id}/qa")
    @Operation(summary = "Ask question about a document")
    public ResponseEntity<QAResponse> askQuestion(@PathVariable UUID id, @RequestBody QAQuery query) {
        return ResponseEntity.ok(qaService.answerQuestion(id, query));
    }
}
