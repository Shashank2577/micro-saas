package com.microsaas.featureflagai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.crosscutting.starter.queue.QueueService;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.featureflagai.domain.FeatureFlag;
import com.microsaas.featureflagai.repository.FeatureFlagRepository;
import com.microsaas.featureflagai.repository.FlagEvaluationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FlagEvaluationServiceTest {

    @Mock
    private FeatureFlagRepository flagRepository;

    @Mock
    private FlagEvaluationRepository evaluationRepository;

    @Mock
    private SegmentTargetingService segmentService;

    @Mock
    private QueueService queueService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FlagEvaluationService flagEvaluationService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void evaluate_FlagNotFound_ReturnsFalse() {
        UUID flagId = UUID.randomUUID();
        when(flagRepository.findByIdAndTenantId(flagId, tenantId)).thenReturn(Optional.empty());

        boolean result = flagEvaluationService.evaluate(flagId, "user1", null);

        assertFalse(result);
    }

    @Test
    void evaluate_FlagDisabled_ReturnsFalse() {
        UUID flagId = UUID.randomUUID();
        FeatureFlag flag = new FeatureFlag();
        flag.setEnabled(false);
        when(flagRepository.findByIdAndTenantId(flagId, tenantId)).thenReturn(Optional.of(flag));

        boolean result = flagEvaluationService.evaluate(flagId, "user1", null);

        assertFalse(result);
    }

    @Test
    void evaluate_FlagEnabled100Pct_ReturnsTrueAndSavesEvaluation() throws Exception {
        UUID flagId = UUID.randomUUID();
        FeatureFlag flag = new FeatureFlag();
        flag.setEnabled(true);
        flag.setRolloutPct(100);
        when(flagRepository.findByIdAndTenantId(flagId, tenantId)).thenReturn(Optional.of(flag));
        when(segmentService.evaluateSegments(any(), any())).thenReturn(false);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        boolean result = flagEvaluationService.evaluate(flagId, "user1", null);

        assertTrue(result);
        verify(evaluationRepository, times(1)).save(any());
        verify(queueService, times(1)).enqueue(eq("featureflagai.flag.evaluated"), anyString(), eq(0));
    }
}
