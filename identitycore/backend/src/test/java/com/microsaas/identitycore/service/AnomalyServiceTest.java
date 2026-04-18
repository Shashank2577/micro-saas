package com.microsaas.identitycore.service;

import com.microsaas.identitycore.model.Anomaly;
import com.microsaas.identitycore.repository.AnomalyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnomalyServiceTest {

    @Mock
    private AnomalyRepository anomalyRepository;

    @InjectMocks
    private AnomalyService anomalyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAnomaly() {
        UUID tenantId = UUID.randomUUID();
        Anomaly anomaly = new Anomaly();

        when(anomalyRepository.save(any(Anomaly.class))).thenReturn(anomaly);

        Anomaly created = anomalyService.createAnomaly(tenantId, anomaly);

        assertNotNull(created);
        assertEquals("OPEN", anomaly.getStatus());
        assertNotNull(anomaly.getDetectedAt());
        verify(anomalyRepository, times(1)).save(anomaly);
    }

    @Test
    void testUpdateAnomalyStatus() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        Anomaly anomaly = new Anomaly();
        anomaly.setId(id);
        anomaly.setTenantId(tenantId);
        anomaly.setStatus("OPEN");

        when(anomalyRepository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(anomaly));
        when(anomalyRepository.save(any(Anomaly.class))).thenReturn(anomaly);

        Anomaly updated = anomalyService.updateAnomalyStatus(id, tenantId, "RESOLVED");

        assertNotNull(updated);
        assertEquals("RESOLVED", updated.getStatus());
        assertNotNull(updated.getResolvedAt());
        verify(anomalyRepository, times(1)).save(anomaly);
    }
}
