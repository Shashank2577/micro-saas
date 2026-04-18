package com.microsaas.queryoptimizer.service;

import com.microsaas.queryoptimizer.repository.QueryExecutionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BaselineCalculationService {

    private final QueryExecutionRepository executionRepository;

    public BaselineCalculationService(QueryExecutionRepository executionRepository) {
        this.executionRepository = executionRepository;
    }

    public BaselineStats getBaselineStats(UUID tenantId, UUID fingerprintId) {
        Double avgExecutionTime = executionRepository.getAverageExecutionTimeMs(tenantId, fingerprintId);
        Long count = executionRepository.getExecutionCount(tenantId, fingerprintId);

        BaselineStats stats = new BaselineStats();
        stats.setAverageExecutionTimeMs(avgExecutionTime != null ? avgExecutionTime : 0.0);
        stats.setExecutionCount(count != null ? count : 0L);
        return stats;
    }

    public static class BaselineStats {
        private Double averageExecutionTimeMs;
        private Long executionCount;

        public Double getAverageExecutionTimeMs() { return averageExecutionTimeMs; }
        public void setAverageExecutionTimeMs(Double averageExecutionTimeMs) { this.averageExecutionTimeMs = averageExecutionTimeMs; }
        public Long getExecutionCount() { return executionCount; }
        public void setExecutionCount(Long executionCount) { this.executionCount = executionCount; }
    }
}
