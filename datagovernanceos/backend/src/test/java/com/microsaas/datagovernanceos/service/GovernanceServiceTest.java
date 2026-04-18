package com.microsaas.datagovernanceos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.datagovernanceos.dto.DataClassificationResult;
import com.microsaas.datagovernanceos.entity.DataAsset;
import com.microsaas.datagovernanceos.repository.ComplianceAuditRepository;
import com.microsaas.datagovernanceos.repository.DataAssetRepository;
import com.microsaas.datagovernanceos.repository.GovernancePolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GovernanceServiceTest {

    @Mock
    private DataAssetRepository dataAssetRepository;
    @Mock
    private GovernancePolicyRepository policyRepository;
    @Mock
    private ComplianceAuditRepository auditRepository;
    @Mock
    private DataClassificationAiService aiService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private GovernanceService governanceService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void createAsset_withAiClassification() {
        DataAsset asset = new DataAsset();
        asset.setName("Customer Data");

        DataClassificationResult aiResult = new DataClassificationResult();
        aiResult.setClassification("CONFIDENTIAL");
        aiResult.setPiiStatus(true);

        when(aiService.classifyAsset(any(DataAsset.class))).thenReturn(aiResult);
        when(dataAssetRepository.save(any(DataAsset.class))).thenAnswer(i -> {
            DataAsset saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        DataAsset savedAsset = governanceService.createAsset(asset);

        assertEquals("CONFIDENTIAL", savedAsset.getClassification());
        assertTrue(savedAsset.getPiiStatus());
        assertEquals(tenantId, savedAsset.getTenantId());
        verify(eventPublisher, times(1)).publishEvent(any(Object.class));
    }
}
