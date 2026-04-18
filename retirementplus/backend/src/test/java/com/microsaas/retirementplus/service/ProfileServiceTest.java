package com.microsaas.retirementplus.service;

import com.microsaas.retirementplus.domain.UserProfile;
import com.microsaas.retirementplus.dto.ProfileDto;
import com.microsaas.retirementplus.repository.UserProfileRepository;
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
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ProfileService profileService;

    private UUID tenantId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    void createOrUpdateProfile_ShouldCreateNewProfile() {
        ProfileDto dto = new ProfileDto();
        dto.setUserId(userId);
        dto.setCurrentAge(60);
        dto.setRetirementAge(67);
        dto.setCurrentSavings(new BigDecimal("500000.00"));
        dto.setDesiredIncome(new BigDecimal("60000.00"));

        when(userProfileRepository.findByUserIdAndTenantId(userId, tenantId)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> {
            UserProfile p = invocation.getArgument(0);
            if(p.getId() == null) p.getId(); // No-op, just to ensure it executes
            return p;
        });

        UserProfile result = profileService.createOrUpdateProfile(dto, tenantId);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals(userId, result.getUserId());
        assertEquals(60, result.getCurrentAge());
        verify(userProfileRepository).save(any(UserProfile.class));
        verify(eventPublisher).publishEvent(any(java.util.Map.class));
    }
}
