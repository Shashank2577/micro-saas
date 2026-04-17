package com.microsaas.datastoryteller.api;

import com.microsaas.datastoryteller.domain.model.NarrativeTemplate;
import com.microsaas.datastoryteller.service.NarrativeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
@Tag(name = "Templates", description = "Narrative Templates")
public class TemplateController {

    private final NarrativeService narrativeService;

    @GetMapping
    @Operation(summary = "List templates")
    public ResponseEntity<List<NarrativeTemplate>> listTemplates(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(narrativeService.listTemplates(tenantId));
    }

    @PostMapping
    @Operation(summary = "Create custom template")
    public ResponseEntity<NarrativeTemplate> createTemplate(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody NarrativeTemplate template) {
        template.setTenantId(tenantId);
        return ResponseEntity.ok(narrativeService.createTemplate(template));
    }
}
