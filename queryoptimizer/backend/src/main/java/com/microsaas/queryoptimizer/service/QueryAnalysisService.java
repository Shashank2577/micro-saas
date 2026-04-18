package com.microsaas.queryoptimizer.service;

import com.microsaas.queryoptimizer.domain.QueryExecution;
import com.microsaas.queryoptimizer.domain.QueryFingerprint;
import com.microsaas.queryoptimizer.dto.QueryLogEntry;
import com.microsaas.queryoptimizer.repository.QueryExecutionRepository;
import com.microsaas.queryoptimizer.repository.QueryFingerprintRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class QueryAnalysisService {

    private final QueryFingerprintRepository fingerprintRepository;
    private final QueryExecutionRepository executionRepository;
    private final AIRecommendationService aiRecommendationService;

    public QueryAnalysisService(QueryFingerprintRepository fingerprintRepository, QueryExecutionRepository executionRepository, AIRecommendationService aiRecommendationService) {
        this.fingerprintRepository = fingerprintRepository;
        this.executionRepository = executionRepository;
        this.aiRecommendationService = aiRecommendationService;
    }

    @Transactional
    public void processQueryLogs(UUID tenantId, List<QueryLogEntry> logEntries) {
        for (QueryLogEntry entry : logEntries) {
            String normalizedQuery = normalizeQuery(entry.getQuery());
            String hash = String.valueOf(normalizedQuery.hashCode());

            QueryFingerprint fingerprint = fingerprintRepository.findByTenantIdAndFingerprintHash(tenantId, hash)
                .orElseGet(() -> {
                    QueryFingerprint newFingerprint = new QueryFingerprint(tenantId, hash, normalizedQuery, entry.getDatabaseType());
                    return fingerprintRepository.save(newFingerprint);
                });

            QueryExecution execution = new QueryExecution(
                tenantId,
                fingerprint,
                entry.getQuery(),
                entry.getExecutionTimeMs(),
                entry.getExecutedAt() != null ? entry.getExecutedAt() : java.time.OffsetDateTime.now()
            );
            execution.setDatabaseUser(entry.getDatabaseUser());
            execution.setExecutionPlan(entry.getExecutionPlan());
            executionRepository.save(execution);
        }

        // Asynchronous AI analysis triggers could go here
    }

    public String normalizeQuery(String rawQuery) {
        if (rawQuery == null) return "";
        // Basic normalization: replace numbers and strings with placeholders
        String normalized = rawQuery.replaceAll("'(?:[^']|'')*'", "?");
        normalized = normalized.replaceAll("\\b\\d+\\b", "?");
        // Simplify whitespaces
        normalized = normalized.replaceAll("\\s+", " ").trim();
        return normalized.toUpperCase();
    }
}
