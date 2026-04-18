package com.microsaas.prospectiq.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.prospectiq.dto.SignalRequest;
import com.microsaas.prospectiq.model.Signal;
import com.microsaas.prospectiq.repository.SignalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SignalServiceTest {

    @Mock
    private SignalRepository signalRepository;

    @InjectMocks
    private SignalService signalService;

    private UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @Test
    void testIngestSignal() {
        SignalRequest request = new SignalRequest();
        request.setProspectId(UUID.randomUUID());
        request.setType("FUNDING");
        request.setSource("TechCrunch");
        request.setContent("Raised Series A");
        request.setDetectedAt(ZonedDateTime.now());

        Signal saved = new Signal();
        saved.setId(UUID.randomUUID());
        saved.setTenantId(tenantId);
        saved.setType("FUNDING");

        when(signalRepository.save(any(Signal.class))).thenReturn(saved);

        Signal result = signalService.ingestSignal(request);

        assertNotNull(result);
        assertEquals("FUNDING", result.getType());
    }

    @Test
    void testGetSignals() {
        UUID prospectId = UUID.randomUUID();
        when(signalRepository.findByTenantIdAndProspectIdOrderByDetectedAtDesc(tenantId, prospectId))
                .thenReturn(List.of(new Signal()));

        List<Signal> signals = signalService.getSignalsForProspect(prospectId);

        assertFalse(signals.isEmpty());
    }
}
