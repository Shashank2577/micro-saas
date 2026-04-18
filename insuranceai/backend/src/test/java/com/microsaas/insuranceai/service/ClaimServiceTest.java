package com.microsaas.insuranceai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.insuranceai.domain.Claim;
import com.microsaas.insuranceai.dto.FraudScoreResult;
import com.microsaas.insuranceai.repository.ClaimRepository;
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

class ClaimServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private AIFraudService aiFraudService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private ClaimService claimService;
    private MockedStatic<TenantContext> tenantContextMock;
    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        claimService = new ClaimService(claimRepository, aiFraudService, eventPublisher);
        
        tenantContextMock = mockStatic(TenantContext.class);
        tenantContextMock.when(TenantContext::require).thenReturn(tenantId);
    }

    @AfterEach
    void tearDown() {
        tenantContextMock.close();
    }

    @Test
    void testGetAllClaims() {
        Claim claim1 = new Claim();
        Claim claim2 = new Claim();
        when(claimRepository.findByTenantId(tenantId)).thenReturn(List.of(claim1, claim2));

        List<Claim> result = claimService.getAllClaims();

        assertEquals(2, result.size());
        verify(claimRepository).findByTenantId(tenantId);
    }

    @Test
    void testGetClaim() {
        UUID claimId = UUID.randomUUID();
        Claim claim = new Claim();
        when(claimRepository.findByIdAndTenantId(claimId, tenantId)).thenReturn(Optional.of(claim));

        Claim result = claimService.getClaim(claimId);

        assertNotNull(result);
        verify(claimRepository).findByIdAndTenantId(claimId, tenantId);
    }

    @Test
    void testCreateClaim() {
        Claim claim = new Claim();
        
        FraudScoreResult fraudScoreResult = new FraudScoreResult();
        fraudScoreResult.setScore(new BigDecimal("10.5"));
        fraudScoreResult.setReasoning("Seems legit");
        
        when(aiFraudService.analyzeClaim(any(Claim.class))).thenReturn(fraudScoreResult);
        when(claimRepository.save(any(Claim.class))).thenAnswer(i -> i.getArguments()[0]);

        Claim result = claimService.createClaim(claim);

        assertNotNull(result.getId());
        assertEquals(tenantId, result.getTenantId());
        assertEquals("NEW", result.getStatus());
        assertEquals(new BigDecimal("10.5"), result.getAiFraudScore());
        assertEquals("Seems legit", result.getAiFraudReasoning());
        
        verify(claimRepository).save(claim);
        verify(eventPublisher).publishEvent(anyMap());
    }
}
