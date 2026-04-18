package com.microsaas.queryoptimizer.controller;

import com.microsaas.queryoptimizer.domain.QueryFingerprint;
import com.microsaas.queryoptimizer.repository.QueryFingerprintRepository;
import com.microsaas.queryoptimizer.service.AIRecommendationService;
import com.microsaas.queryoptimizer.service.BaselineCalculationService;
import com.microsaas.queryoptimizer.service.QueryAnalysisService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryControllerTest {

    @Mock
    private QueryAnalysisService queryAnalysisService;

    @Mock
    private QueryFingerprintRepository fingerprintRepository;

    @Mock
    private BaselineCalculationService baselineService;

    @Mock
    private AIRecommendationService aiRecommendationService;

    @InjectMocks
    private QueryController queryController;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
    }

    @Test
    void testGetFingerprints() {
        try (MockedStatic<TenantContext> mocked = Mockito.mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::require).thenReturn(tenantId);

            QueryFingerprint fp = new QueryFingerprint();
            Page<QueryFingerprint> page = new PageImpl<>(List.of(fp));
            when(fingerprintRepository.findByTenantId(eq(tenantId), any(Pageable.class))).thenReturn(page);

            ResponseEntity<Page<QueryFingerprint>> response = queryController.getFingerprints(Pageable.unpaged());

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(1, response.getBody().getTotalElements());
        }
    }
}
