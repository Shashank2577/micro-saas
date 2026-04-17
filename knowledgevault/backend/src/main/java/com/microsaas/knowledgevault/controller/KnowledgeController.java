package com.microsaas.knowledgevault.controller;

import com.microsaas.knowledgevault.domain.KnowledgeDocument;
import com.microsaas.knowledgevault.domain.KnowledgeQuery;
import com.microsaas.knowledgevault.service.KnowledgeService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @PostMapping("/documents")
    public ResponseEntity<KnowledgeDocument> addDocument(@RequestBody KnowledgeDocument document) {
        document.setTenantId(TenantContext.require().toString());
        if (document.getFreshnessStatus() == null) {
             document.setFreshnessStatus("FRESH");
        }
        return ResponseEntity.ok(knowledgeService.addDocument(document));
    }

    @GetMapping("/query")
    public ResponseEntity<String> answerQuery(@RequestParam String q) {
        String tenantId = TenantContext.require().toString();
        return ResponseEntity.ok(knowledgeService.answerQuery(tenantId, q));
    }

    @GetMapping("/top-queries")
    public ResponseEntity<List<KnowledgeQuery>> getTopQueries() {
        String tenantId = TenantContext.require().toString();
        return ResponseEntity.ok(knowledgeService.getTopQueries(tenantId));
    }
}
