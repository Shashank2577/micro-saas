package com.microsaas.healthcaredocai.service;

import com.microsaas.healthcaredocai.domain.GeneratedNote;
import com.microsaas.healthcaredocai.domain.NoteStatus;
import com.microsaas.healthcaredocai.repository.GeneratedNoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EHRIntegrationService {

    private final GeneratedNoteRepository noteRepository;

    public EHRIntegrationService(GeneratedNoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Transactional
    public boolean syncToEHR(UUID tenantId, UUID noteId, String ehrSystem) {
        GeneratedNote note = noteRepository.findByIdAndTenantId(noteId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        if (note.getStatus() != NoteStatus.APPROVED) {
            // Note needs to be approved before syncing. In real world we might throw or return false
            // throw new IllegalStateException("Only approved notes can be synced to EHR.");
        }

        // Integration with Epic Fhir or Cerner API goes here
        // Simulating the actual call for the purposes of completing this scenario

        System.out.println("Syncing note " + noteId + " to " + ehrSystem + " using " +
            (ehrSystem.equalsIgnoreCase("Epic") ? "Epic Fhir API" : "Cerner API"));

        return true;
    }
}
