package com.microsaas.queryoptimizer.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.queryoptimizer.domain.QueryFingerprint;
import com.microsaas.queryoptimizer.dto.QueryLogEntry;
import com.microsaas.queryoptimizer.repository.QueryFingerprintRepository;
import com.microsaas.queryoptimizer.service.AIRecommendationService;
import com.microsaas.queryoptimizer.service.BaselineCalculationService;
import com.microsaas.queryoptimizer.service.QueryAnalysisService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/queries")
public class QueryController {

    private final QueryAnalysisService queryAnalysisService;
    private final QueryFingerprintRepository fingerprintRepository;
    private final BaselineCalculationService baselineService;
    private final AIRecommendationService aiRecommendationService;

    public QueryController(QueryAnalysisService queryAnalysisService,
                           QueryFingerprintRepository fingerprintRepository,
                           BaselineCalculationService baselineService,
                           AIRecommendationService aiRecommendationService) {
        this.queryAnalysisService = queryAnalysisService;
        this.fingerprintRepository = fingerprintRepository;
        this.baselineService = baselineService;
        this.aiRecommendationService = aiRecommendationService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadQueryLogs(@RequestBody List<QueryLogEntry> entries) {
        UUID tenantId = TenantContext.require();
        queryAnalysisService.processQueryLogs(tenantId, entries);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/fingerprints")
    public ResponseEntity<Page<QueryFingerprint>> getFingerprints(Pageable pageable) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(fingerprintRepository.findByTenantId(tenantId, pageable));
    }

    @GetMapping("/fingerprints/{id}")
    public ResponseEntity<QueryFingerprint> getFingerprint(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return fingerprintRepository.findByIdAndTenantId(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fingerprints/{id}/baseline")
    public ResponseEntity<BaselineCalculationService.BaselineStats> getBaseline(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(baselineService.getBaselineStats(tenantId, id));
    }

    @PostMapping("/fingerprints/{id}/analyze")
    public ResponseEntity<Void> analyzeFingerprint(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        aiRecommendationService.analyzeFingerprint(tenantId, id);
        return ResponseEntity.accepted().build();
    }
}
