package com.microsaas.datacatalogai.api;

import com.microsaas.datacatalogai.domain.model.Relationship;
import com.microsaas.datacatalogai.service.RelationshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/relationships")
@RequiredArgsConstructor
@Tag(name = "Relationships", description = "Endpoints for managing asset relationships manually")
public class RelationshipController {

    private final RelationshipService relationshipService;

    @PostMapping
    @Operation(summary = "Manually create a relationship between assets")
    public Relationship createRelationship(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody Relationship relationship) {
        return relationshipService.createRelationship(tenantId, relationship);
    }
}
