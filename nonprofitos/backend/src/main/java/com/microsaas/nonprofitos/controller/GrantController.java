package com.microsaas.nonprofitos.controller;

import com.microsaas.nonprofitos.domain.Grant;
import com.microsaas.nonprofitos.dto.GrantDto;
import com.microsaas.nonprofitos.service.GrantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/grants")
public class GrantController {

    private final GrantService grantService;

    public GrantController(GrantService grantService) {
        this.grantService = grantService;
    }

    @GetMapping
    public ResponseEntity<List<Grant>> getAllGrants() {
        return ResponseEntity.ok(grantService.getAllGrants());
    }

    @PostMapping
    public ResponseEntity<Grant> createGrant(@RequestBody GrantDto dto) {
        return ResponseEntity.ok(grantService.createGrant(dto));
    }

    @PostMapping("/{id}/generate-draft")
    public ResponseEntity<String> generateDraft(@PathVariable UUID id) {
        return ResponseEntity.ok(grantService.generateDraft(id));
    }
}
