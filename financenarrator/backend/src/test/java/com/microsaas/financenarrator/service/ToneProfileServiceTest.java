package com.microsaas.financenarrator.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.financenarrator.model.ToneProfile;
import com.microsaas.financenarrator.repository.ToneProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToneProfileServiceTest {

    @Mock
    private ToneProfileRepository repository;

    @Mock
    private EventPublisherService eventPublisher;

    @InjectMocks
    private ToneProfileService service;

    private MockedStatic<TenantContext> tenantContextMockedStatic;
    private UUID tenantId;
    private UUID profileId;
    private ToneProfile profile;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        profileId = UUID.randomUUID();
        tenantContextMockedStatic = Mockito.mockStatic(TenantContext.class);
        tenantContextMockedStatic.when(TenantContext::require).thenReturn(tenantId);

        profile = new ToneProfile();
        profile.setId(profileId);
        profile.setTenantId(tenantId);
        profile.setName("Executive Summary Tone");
        profile.setStatus("DRAFT");
    }

    @AfterEach
    void tearDown() {
        tenantContextMockedStatic.close();
    }

    @Test
    void testCreateToneProfile_success() {
        when(repository.save(any(ToneProfile.class))).thenReturn(profile);

        ToneProfile result = service.create(profile);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(repository, times(1)).save(profile);
        verify(eventPublisher, times(1)).publishEvent(
                eq("financenarrator.narrative.generated"),
                eq(tenantId),
                eq("financenarrator"),
                any(Map.class)
        );
    }

    @Test
    void testGetToneProfile_byId() {
        when(repository.findByIdAndTenantId(profileId, tenantId)).thenReturn(Optional.of(profile));

        ToneProfile result = service.getById(profileId);

        assertNotNull(result);
        assertEquals(profileId, result.getId());
        verify(repository, times(1)).findByIdAndTenantId(profileId, tenantId);
    }

    @Test
    void testListToneProfiles_byTenant() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(profile));

        List<ToneProfile> result = service.list();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository, times(1)).findByTenantId(tenantId);
    }
}
