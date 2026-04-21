package com.microsaas.documentintelligence.controller;

import com.microsaas.documentintelligence.dto.SearchQuery;
import com.microsaas.documentintelligence.model.DocumentChunk;
import com.microsaas.documentintelligence.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Document Search API")
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    @Operation(summary = "Semantic search across documents")
    public ResponseEntity<List<DocumentChunk>> search(@RequestBody SearchQuery query) {
        return ResponseEntity.ok(searchService.search(query));
    }
}
