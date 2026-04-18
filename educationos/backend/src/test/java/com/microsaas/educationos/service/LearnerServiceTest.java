package com.microsaas.educationos.service;

import com.microsaas.educationos.domain.entity.LearnerProfile;
import com.microsaas.educationos.domain.repository.LearnerProfileRepository;
import com.microsaas.educationos.dto.LearnerProfileDto;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LearnerServiceTest {

    @Mock
    private LearnerProfileRepository repository;

    @InjectMocks
    private LearnerService service;

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
    void testGetLearnerProfile() {
        UUID userId = UUID.randomUUID();
        LearnerProfile profile = new LearnerProfile();
        profile.setUserId(userId);
        profile.setBackgroundInfo("Background");

        when(repository.findByUserIdAndTenantId(userId, tenantId)).thenReturn(Optional.of(profile));

        LearnerProfileDto result = service.getLearnerProfile(userId);
        assertNotNull(result);
        assertEquals("Background", result.getBackgroundInfo());
    }
}
