package com.microsaas.healthcaredocai.service;

import com.microsaas.healthcaredocai.domain.ClinicalEncounter;
import com.microsaas.healthcaredocai.repository.ClinicalEncounterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class TranscriptionService {

    private final ClinicalEncounterRepository encounterRepository;

    public TranscriptionService(ClinicalEncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    @Transactional
    public ClinicalEncounter transcribeEncounter(UUID tenantId, String patientId, String clinicianId, byte[] audioData) {
        // Google Cloud Speech API integration would go here.
        // For the purposes of this task, simulating the Google Cloud Speech API medical model behavior
        String transcript = "Patient presents with symptoms of acute pharyngitis. Complains of sore throat and difficulty swallowing for 3 days. Denies fever. Physical exam shows erythematous tonsils without exudate. Plan: symptomatic treatment, warm salt water gargles. Follow-up in one week if symptoms persist.";

        ClinicalEncounter encounter = new ClinicalEncounter();
        encounter.setTenantId(tenantId);
        encounter.setPatientId(patientId);
        encounter.setClinicianId(clinicianId);
        encounter.setTranscript(transcript);
        encounter.setTimestamp(OffsetDateTime.now());

        return encounterRepository.save(encounter);
    }
}
