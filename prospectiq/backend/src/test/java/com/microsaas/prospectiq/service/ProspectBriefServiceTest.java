package com.microsaas.prospectiq.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.prospectiq.client.LiteLLMClient;
import com.microsaas.prospectiq.model.Prospect;
import com.microsaas.prospectiq.model.ProspectBrief;
import com.microsaas.prospectiq.model.Signal;
import com.microsaas.prospectiq.repository.ProspectBriefRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProspectBriefServiceTest {

    @Mock
    private ProspectBriefRepository prospectBriefRepository;
    @Mock
    private ProspectService prospectService;
    @Mock
    private SignalService signalService;
    @Mock
    private LiteLLMClient liteLLMClient;

    @InjectMocks
    private ProspectBriefService prospectBriefService;

    private UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @Test
    void testGenerateBrief() {
        UUID prospectId = UUID.randomUUID();
        Prospect p = new Prospect();
        p.setName("Test");
        when(prospectService.getProspect(prospectId)).thenReturn(p);
        when(signalService.getSignalsForProspect(prospectId)).thenReturn(List.of(new Signal()));
        when(liteLLMClient.generateBrief(anyString())).thenReturn("AI Content");

        ProspectBrief saved = new ProspectBrief();
        saved.setContent("AI Content");
        when(prospectBriefRepository.save(any(ProspectBrief.class))).thenReturn(saved);

        ProspectBrief brief = prospectBriefService.generateBrief(prospectId);

        assertNotNull(brief);
        assertEquals("AI Content", brief.getContent());
    }

    @Test
    void testGetLatestBrief() {
        UUID prospectId = UUID.randomUUID();
        when(prospectBriefRepository.findFirstByTenantIdAndProspectIdOrderByCreatedAtDesc(tenantId, prospectId))
                .thenReturn(Optional.of(new ProspectBrief()));

        ProspectBrief b = prospectBriefService.getLatestBrief(prospectId);

        assertNotNull(b);
    }
}
