package com.microsaas.datacatalogai.api;

import com.microsaas.datacatalogai.domain.model.GlossaryTerm;
import com.microsaas.datacatalogai.service.GlossaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/glossary")
@RequiredArgsConstructor
@Tag(name = "Glossary", description = "Endpoints for managing the business glossary")
public class GlossaryController {

    private final GlossaryService glossaryService;

    @PostMapping
    @Operation(summary = "Create a new glossary term")
    public GlossaryTerm createTerm(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody GlossaryTerm term) {
        return glossaryService.createTerm(tenantId, term);
    }

    @GetMapping
    @Operation(summary = "List all glossary terms")
    public List<GlossaryTerm> listTerms(@RequestHeader("X-Tenant-ID") String tenantId) {
        return glossaryService.listTerms(tenantId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a glossary term")
    public GlossaryTerm updateTerm(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id, @RequestBody GlossaryTerm term) {
        return glossaryService.updateTerm(tenantId, id, term);
    }
}
