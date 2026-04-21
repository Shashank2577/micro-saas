package com.microsaas.contextlayer.controller;

import com.microsaas.contextlayer.service.ContextEnrichmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enrichment")
@RequiredArgsConstructor
public class ContextEnrichmentController {

    private final ContextEnrichmentService contextEnrichmentService;

    @PostMapping("/{customerId}")
    public ResponseEntity<Void> enrichContext(@PathVariable String customerId) {
        contextEnrichmentService.enrichContext(customerId);
        return ResponseEntity.ok().build();
    }
}
