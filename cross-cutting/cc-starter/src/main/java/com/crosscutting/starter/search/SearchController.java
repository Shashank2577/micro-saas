package com.crosscutting.starter.search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cc/search")
@Tag(name = "Search", description = "Full-text, semantic, and hybrid search over indexed resources")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/index")
    @Operation(summary = "Index a resource", description = "Add or update a resource in the search index for a given tenant")
    @ApiResponse(responseCode = "200", description = "Resource indexed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Void> index(@Valid @RequestBody IndexRequest request) {
        searchService.index(
                request.tenantId(),
                request.resourceType(),
                request.resourceId(),
                request.content(),
                request.metadata()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Full-text search", description = "Search indexed resources by query string with optional type filter")
    @ApiResponse(responseCode = "200", description = "Search results returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public List<SearchResult> search(
            @Parameter(description = "Tenant ID to search within") @RequestParam UUID tenantId,
            @Parameter(description = "Search query string") @RequestParam String q,
            @Parameter(description = "Filter by resource type") @RequestParam(required = false) String type,
            @Parameter(description = "Maximum number of results") @RequestParam(defaultValue = "20") int limit) {
        return searchService.search(tenantId, q, type, limit);
    }

    @PostMapping("/semantic")
    @Operation(summary = "Semantic search", description = "Search using a vector embedding for similarity-based retrieval")
    @ApiResponse(responseCode = "200", description = "Semantic search results returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public List<SearchResult> semanticSearch(@Valid @RequestBody SemanticSearchRequest request) {
        return searchService.semanticSearch(
                request.tenantId(),
                request.embedding(),
                request.resourceType(),
                request.limit() > 0 ? request.limit() : 20
        );
    }

    @PostMapping("/hybrid")
    @Operation(summary = "Hybrid search", description = "Combine full-text and semantic search with configurable weights")
    @ApiResponse(responseCode = "200", description = "Hybrid search results returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public List<SearchResult> hybridSearch(@Valid @RequestBody HybridSearchRequest request) {
        return searchService.hybridSearch(
                request.tenantId(),
                request.query(),
                request.embedding(),
                request.textWeight() > 0 ? request.textWeight() : 0.5,
                request.vectorWeight() > 0 ? request.vectorWeight() : 0.5,
                request.resourceType(),
                request.limit() > 0 ? request.limit() : 20
        );
    }

    @DeleteMapping("/index")
    @Operation(summary = "Delete a resource from index", description = "Remove a specific resource from the search index")
    @ApiResponse(responseCode = "200", description = "Resource removed from index")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Void> deleteIndex(@Valid @RequestBody DeleteIndexRequest request) {
        searchService.deleteIndex(
                request.tenantId(),
                request.resourceType(),
                request.resourceId()
        );
        return ResponseEntity.ok().build();
    }

    // --- Request DTOs ---

    public record IndexRequest(
            @NotNull(message = "Tenant ID is required") UUID tenantId,
            @NotBlank(message = "Resource type is required") String resourceType,
            @NotNull(message = "Resource ID is required") UUID resourceId,
            @NotBlank(message = "Content is required") String content,
            String metadata
    ) {}

    public record SemanticSearchRequest(
            @NotNull(message = "Tenant ID is required") UUID tenantId,
            @NotNull(message = "Embedding is required") double[] embedding,
            String resourceType,
            int limit
    ) {}

    public record HybridSearchRequest(
            @NotNull(message = "Tenant ID is required") UUID tenantId,
            @NotBlank(message = "Query is required") String query,
            @NotNull(message = "Embedding is required") double[] embedding,
            double textWeight,
            double vectorWeight,
            String resourceType,
            int limit
    ) {}

    public record DeleteIndexRequest(
            @NotNull(message = "Tenant ID is required") UUID tenantId,
            @NotBlank(message = "Resource type is required") String resourceType,
            @NotNull(message = "Resource ID is required") UUID resourceId
    ) {}
}
