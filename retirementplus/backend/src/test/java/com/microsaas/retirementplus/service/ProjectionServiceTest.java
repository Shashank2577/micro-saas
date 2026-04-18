package com.microsaas.retirementplus.service;

import com.microsaas.retirementplus.domain.Projection;
import com.microsaas.retirementplus.domain.UserProfile;
import com.microsaas.retirementplus.dto.ProjectionResponseDto;
import com.microsaas.retirementplus.repository.ProjectionRepository;
import com.microsaas.retirementplus.repository.UserProfileRepository;
import com.microsaas.retirementplus.ai.LiteLLMClient;
import com.microsaas.retirementplus.ai.AiProjectionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProjectionServiceTest {

    @Mock
    private ProjectionRepository projectionRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private LiteLLMClient liteLLMClient;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ProjectionService projectionService;

    private UUID tenantId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    void generateProjection_ShouldGenerateProjection() {
        UserProfile profile = new UserProfile();
        profile.setUserId(userId);
        profile.setTenantId(tenantId);
        profile.setCurrentAge(60);
        profile.setRetirementAge(67);
        profile.setCurrentSavings(new BigDecimal("500000.00"));
        profile.setDesiredIncome(new BigDecimal("60000.00"));
        profile.setGender("MALE");
        profile.setHealthStatus("HEALTHY");
        profile.setFamilyHistory("MODERATE");

        AiProjectionResult aiResult = new AiProjectionResult();
        aiResult.setLifeExpectancy(89);
        aiResult.setSocialSecurityClaimingAge(67);
        aiResult.setEstimatedHealthcareCost(new BigDecimal("300000.00"));
        aiResult.setQcdOpportunityAge(72);

        when(userProfileRepository.findByUserIdAndTenantId(userId, tenantId)).thenReturn(Optional.of(profile));
        when(liteLLMClient.getProjections(anyString())).thenReturn(aiResult);
        when(projectionRepository.findByUserIdAndTenantId(userId, tenantId)).thenReturn(Optional.empty());
        when(projectionRepository.save(any(Projection.class))).thenAnswer(invocation -> {
            Projection p = invocation.getArgument(0);
            if(p.getId() == null) p.getId();
            return p;
        });

        ProjectionResponseDto result = projectionService.generateProjection(userId, tenantId);

        assertNotNull(result);
        assertEquals(89, result.getLifeExpectancy());
        assertEquals(0, new BigDecimal("12.00").compareTo(result.getSafeWithdrawalRate())); // 60k/500k * 100
        assertEquals(67, result.getSocialSecurityClaimingAge());
        verify(projectionRepository).save(any(Projection.class));
        verify(eventPublisher).publishEvent(any(java.util.Map.class));
    }
}
