package com.microsaas.healthcaredocai.service;

import com.microsaas.healthcaredocai.domain.ClinicalEncounter;
import com.microsaas.healthcaredocai.domain.ClinicalTemplate;
import com.microsaas.healthcaredocai.domain.GeneratedNote;
import com.microsaas.healthcaredocai.domain.NoteStatus;
import com.microsaas.healthcaredocai.domain.NoteType;
import com.microsaas.healthcaredocai.repository.ClinicalEncounterRepository;
import com.microsaas.healthcaredocai.repository.ClinicalTemplateRepository;
import com.microsaas.healthcaredocai.repository.GeneratedNoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteGenerationServiceTest {

    @Mock
    private GeneratedNoteRepository noteRepository;

    @Mock
    private ClinicalEncounterRepository encounterRepository;

    @Mock
    private ClinicalTemplateRepository templateRepository;

    @InjectMocks
    private NoteGenerationService service;

    @Test
    public void testGenerateNote() {
        UUID tenantId = UUID.randomUUID();
        UUID encounterId = UUID.randomUUID();

        ClinicalEncounter encounter = new ClinicalEncounter();
        encounter.setTenantId(tenantId);
        encounter.setTranscript("Test transcript");

        when(encounterRepository.findByIdAndTenantId(encounterId, tenantId)).thenReturn(Optional.of(encounter));
        when(templateRepository.findByTenantIdAndSpecialtyAndNoteType(tenantId, "General", NoteType.SOAP)).thenReturn(Optional.empty());

        when(noteRepository.save(any(GeneratedNote.class))).thenAnswer(i -> {
            GeneratedNote note = (GeneratedNote) i.getArguments()[0];
            note.setId(UUID.randomUUID());
            return note;
        });

        GeneratedNote note = service.generateNote(tenantId, encounterId, NoteType.SOAP, "General");

        assertNotNull(note);
        assertEquals(NoteStatus.DRAFT, note.getStatus());
        assertTrue(note.getContent().contains("Test transcript"));
    }
}
