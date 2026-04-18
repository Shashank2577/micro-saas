package com.microsaas.queryoptimizer.service;

import com.microsaas.queryoptimizer.domain.QueryFingerprint;
import com.microsaas.queryoptimizer.dto.QueryLogEntry;
import com.microsaas.queryoptimizer.repository.QueryExecutionRepository;
import com.microsaas.queryoptimizer.repository.QueryFingerprintRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryAnalysisServiceTest {

    @Mock
    private QueryFingerprintRepository fingerprintRepository;

    @Mock
    private QueryExecutionRepository executionRepository;

    @Mock
    private AIRecommendationService aiRecommendationService;

    @InjectMocks
    private QueryAnalysisService queryAnalysisService;

    @Test
    void shouldProcessQueryLogsAndCreateFingerprint() {
        UUID tenantId = UUID.randomUUID();
        QueryLogEntry entry = new QueryLogEntry();
        entry.setQuery("SELECT * FROM users WHERE id = 1");
        entry.setExecutionTimeMs(150.0);
        entry.setDatabaseType("PostgreSQL");

        when(fingerprintRepository.findByTenantIdAndFingerprintHash(eq(tenantId), anyString()))
                .thenReturn(Optional.empty());

        when(fingerprintRepository.save(any(QueryFingerprint.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        queryAnalysisService.processQueryLogs(tenantId, List.of(entry));

        verify(fingerprintRepository, times(1)).save(any(QueryFingerprint.class));
        verify(executionRepository, times(1)).save(any());
    }
}
