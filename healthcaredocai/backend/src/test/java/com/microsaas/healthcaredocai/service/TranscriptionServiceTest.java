package com.microsaas.healthcaredocai.service;

import com.microsaas.healthcaredocai.domain.ClinicalEncounter;
import com.microsaas.healthcaredocai.repository.ClinicalEncounterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TranscriptionServiceTest {

    @Mock
    private ClinicalEncounterRepository repository;

    @InjectMocks
    private TranscriptionService service;

    @Test
    public void testTranscribeEncounter() {
        UUID tenantId = UUID.randomUUID();
        when(repository.save(any(ClinicalEncounter.class))).thenAnswer(i -> {
            ClinicalEncounter enc = (ClinicalEncounter) i.getArguments()[0];
            enc.setId(UUID.randomUUID());
            return enc;
        });

        ClinicalEncounter result = service.transcribeEncounter(tenantId, "patient1", "clinician1", new byte[0]);

        assertNotNull(result);
        assertEquals("patient1", result.getPatientId());
        assertEquals("clinician1", result.getClinicianId());
        assertNotNull(result.getTranscript());
    }
}
