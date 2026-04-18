package com.microsaas.insuranceai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.insuranceai.domain.Policy;
import com.microsaas.insuranceai.dto.RiskScoreResult;
import com.microsaas.insuranceai.repository.PolicyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private AIRiskService aiRiskService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private PolicyService policyService;
    private MockedStatic<TenantContext> tenantContextMock;
    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        policyService = new PolicyService(policyRepository, aiRiskService, eventPublisher);
        
        tenantContextMock = mockStatic(TenantContext.class);
        tenantContextMock.when(TenantContext::require).thenReturn(tenantId);
    }

    @AfterEach
    void tearDown() {
        tenantContextMock.close();
    }

    @Test
    void testGetAllPolicies() {
        Policy p1 = new Policy();
        Policy p2 = new Policy();
        when(policyRepository.findByTenantId(tenantId)).thenReturn(List.of(p1, p2));

        List<Policy> result = policyService.getAllPolicies();

        assertEquals(2, result.size());
        verify(policyRepository).findByTenantId(tenantId);
    }

    @Test
    void testGetPolicy() {
        UUID policyId = UUID.randomUUID();
        Policy policy = new Policy();
        when(policyRepository.findByIdAndTenantId(policyId, tenantId)).thenReturn(Optional.of(policy));

        Policy result = policyService.getPolicy(policyId);

        assertNotNull(result);
        verify(policyRepository).findByIdAndTenantId(policyId, tenantId);
    }

    @Test
    void testCreatePolicy() {
        Policy policy = new Policy();
        
        RiskScoreResult riskScoreResult = new RiskScoreResult();
        riskScoreResult.setScore(new BigDecimal("15.5"));
        riskScoreResult.setFactors("Low risk factors");
        
        when(aiRiskService.analyzePolicy(any(Policy.class))).thenReturn(riskScoreResult);
        when(policyRepository.save(any(Policy.class))).thenAnswer(i -> i.getArguments()[0]);

        Policy result = policyService.createPolicy(policy);

        assertNotNull(result.getId());
        assertEquals(tenantId, result.getTenantId());
        assertEquals(new BigDecimal("15.5"), result.getAiRiskScore());
        assertEquals("Low risk factors", result.getAiRiskFactors());
        
        verify(policyRepository).save(policy);
        verify(eventPublisher).publishEvent(anyMap());
    }
}
