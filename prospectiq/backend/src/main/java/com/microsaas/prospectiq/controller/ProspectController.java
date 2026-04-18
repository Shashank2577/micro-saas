package com.microsaas.prospectiq.controller;

import com.microsaas.prospectiq.dto.ProspectRequest;
import com.microsaas.prospectiq.model.Prospect;
import com.microsaas.prospectiq.service.ProspectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prospects")
@RequiredArgsConstructor
@Tag(name = "Prospects")
public class ProspectController {

    private final ProspectService prospectService;

    @GetMapping
    @Operation(summary = "List prospects")
    public ResponseEntity<List<Prospect>> listProspects(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String region) {
        return ResponseEntity.ok(prospectService.getAllProspects(industry, region));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get prospect details")
    public ResponseEntity<Prospect> getProspect(@PathVariable UUID id) {
        return ResponseEntity.ok(prospectService.getProspect(id));
    }

    @PostMapping
    @Operation(summary = "Create a prospect")
    public ResponseEntity<Prospect> createProspect(@RequestBody ProspectRequest request) {
        return ResponseEntity.ok(prospectService.createProspect(request));
    }

    @PostMapping("/{id}/sync")
    @Operation(summary = "Sync prospect with CRM")
    public ResponseEntity<Void> syncCrm(@PathVariable UUID id) {
        // Mock sync for now
        return ResponseEntity.ok().build();
    }
}
