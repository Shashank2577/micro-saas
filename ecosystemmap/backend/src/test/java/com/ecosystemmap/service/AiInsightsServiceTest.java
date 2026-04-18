package com.ecosystemmap.service;

import com.ecosystemmap.domain.IntegrationOpportunity;
import com.ecosystemmap.dto.EcosystemMapDto;
import com.ecosystemmap.repository.IntegrationOpportunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AiInsightsServiceTest {

    @Mock
    private IntegrationOpportunityRepository integrationOpportunityRepository;

    @Mock
    private EcosystemService ecosystemService;

    @InjectMocks
    private AiInsightsService aiInsightsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAnalyzeEcosystem() {
        UUID tenantId = UUID.randomUUID();
        when(ecosystemService.getEcosystemMap(tenantId)).thenReturn(new EcosystemMapDto());
        when(integrationOpportunityRepository.save(any(IntegrationOpportunity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(integrationOpportunityRepository.findByTenantId(tenantId)).thenReturn(List.of(new IntegrationOpportunity()));

        List<IntegrationOpportunity> result = aiInsightsService.analyzeEcosystem(tenantId);
        assertEquals(1, result.size());
    }
}
