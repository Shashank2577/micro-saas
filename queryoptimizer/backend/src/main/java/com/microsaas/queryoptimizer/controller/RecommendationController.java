package com.microsaas.queryoptimizer.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.queryoptimizer.domain.IndexSuggestion;
import com.microsaas.queryoptimizer.domain.QueryRecommendation;
import com.microsaas.queryoptimizer.repository.IndexSuggestionRepository;
import com.microsaas.queryoptimizer.repository.QueryRecommendationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RecommendationController {

    private final QueryRecommendationRepository recommendationRepository;
    private final IndexSuggestionRepository indexSuggestionRepository;

    public RecommendationController(QueryRecommendationRepository recommendationRepository, IndexSuggestionRepository indexSuggestionRepository) {
        this.recommendationRepository = recommendationRepository;
        this.indexSuggestionRepository = indexSuggestionRepository;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<QueryRecommendation>> getRecommendations() {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(recommendationRepository.findByTenantId(tenantId));
    }

    @PutMapping("/recommendations/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        UUID tenantId = TenantContext.require();
        return recommendationRepository.findByIdAndTenantId(id, tenantId)
                .map(rec -> {
                    rec.setStatus(body.get("status"));
                    recommendationRepository.save(rec);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/indexes")
    public ResponseEntity<List<IndexSuggestion>> getIndexSuggestions() {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(indexSuggestionRepository.findByTenantId(tenantId));
    }
}
