package com.microsaas.ghostwriter.controller;

import com.microsaas.ghostwriter.model.ContentRequest;
import com.microsaas.ghostwriter.service.ContentGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/generate")
@RequiredArgsConstructor
public class ContentGenerationController {
    private final ContentGenerationService service;

    @PostMapping
    public ResponseEntity<ContentRequest> generateContent(@RequestBody ContentRequest request, @RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.generateContent(request, tenantId));
    }
}
