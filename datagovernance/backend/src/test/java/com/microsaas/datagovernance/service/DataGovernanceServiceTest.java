package com.microsaas.datagovernance.service;

import com.microsaas.datagovernance.model.*;
import com.microsaas.datagovernance.repository.*;
import com.microsaas.datagovernance.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.crosscutting.starter.tenancy.TenantContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataGovernanceServiceTest {

    @Mock
    private DataRetentionPolicyRepository policyRepository;

    @Mock
    private DataSubjectRequestRepository dsarRepository;
    
    @Mock
    private ConsentRecordRepository consentRepository;

    @InjectMocks
    private DataGovernanceService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void testCreatePolicy() {
        DataRetentionPolicy policy = new DataRetentionPolicy();
        policy.setDataType("USER_DATA");
        policy.setRetentionDays(90);

        when(policyRepository.save(any(DataRetentionPolicy.class))).thenAnswer(i -> i.getArguments()[0]);

        DataRetentionPolicy saved = service.createPolicy(policy);
        
        assertNotNull(saved.getId());
        assertEquals(tenantId, saved.getTenantId());
        assertEquals("USER_DATA", saved.getDataType());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void testCreateAndProcessDsar() {
        DataSubjectRequest dsar = new DataSubjectRequest();
        dsar.setSubjectEmail("test@example.com");
        dsar.setRequestType("ACCESS");

        when(dsarRepository.save(any(DataSubjectRequest.class))).thenAnswer(i -> i.getArguments()[0]);

        DataSubjectRequest created = service.createDsar(dsar);
        
        assertEquals("PENDING", created.getStatus());
        assertEquals(tenantId, created.getTenantId());
        
        created.setId(UUID.randomUUID());
        when(dsarRepository.findById(created.getId())).thenReturn(Optional.of(created));
        
        DataSubjectRequest processed = service.processDsar(created.getId());
        assertEquals("COMPLETED", processed.getStatus());
        assertNotNull(processed.getCompletedAt());
    }
    
    @Test
    void testCreateConsent() {
        ConsentRecord consent = new ConsentRecord();
        consent.setUserEmail("user@test.com");
        consent.setProcessingPurpose("MARKETING");
        consent.setGranted(true);
        
        when(consentRepository.save(any(ConsentRecord.class))).thenAnswer(i -> i.getArguments()[0]);
        
        ConsentRecord saved = service.createConsent(consent);
        assertEquals(tenantId, saved.getTenantId());
        assertTrue(saved.getGranted());
    }
}
