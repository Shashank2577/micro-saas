package com.microsaas.insightengine.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.insightengine.entity.Insight;
import com.microsaas.insightengine.repository.InsightCommentRepository;
import com.microsaas.insightengine.repository.InsightRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InsightControllerTest {

    @Mock
    private InsightRepository insightRepository;

    @Mock
    private InsightCommentRepository insightCommentRepository;

    @InjectMocks
    private InsightController insightController;

    private UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetInsights() {
        Insight insight = new Insight();
        when(insightRepository.findByTenantIdOrderByImpactScoreDesc(tenantId)).thenReturn(List.of(insight));

        List<Insight> result = insightController.getInsights();
        assertEquals(1, result.size());
        verify(insightRepository).findByTenantIdOrderByImpactScoreDesc(tenantId);
    }

    @Test
    void testGetInsight() {
        UUID id = UUID.randomUUID();
        Insight insight = new Insight();
        when(insightRepository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(insight));

        ResponseEntity<Insight> response = insightController.getInsight(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(insight, response.getBody());
    }
}
