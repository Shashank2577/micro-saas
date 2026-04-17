package com.microsaas.healthcaredocai.controller;

import com.microsaas.healthcaredocai.domain.ClinicalEncounter;
import com.microsaas.healthcaredocai.domain.GeneratedNote;
import com.microsaas.healthcaredocai.domain.NoteStatus;
import com.microsaas.healthcaredocai.domain.NoteType;
import com.microsaas.healthcaredocai.repository.ClinicalTemplateRepository;
import com.microsaas.healthcaredocai.service.EHRIntegrationService;
import com.microsaas.healthcaredocai.service.NoteGenerationService;
import com.microsaas.healthcaredocai.service.TranscriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthcareDocAIController.class)
@AutoConfigureMockMvc(addFilters = false)
public class HealthcareDocAIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranscriptionService transcriptionService;

    @MockBean
    private NoteGenerationService noteGenerationService;

    @MockBean
    private EHRIntegrationService ehrIntegrationService;

    @MockBean
    private ClinicalTemplateRepository clinicalTemplateRepository;

    @Test
    public void testTranscribe() throws Exception {
        UUID tenantId = UUID.randomUUID();
        ClinicalEncounter encounter = new ClinicalEncounter();
        encounter.setId(UUID.randomUUID());
        encounter.setTenantId(tenantId);
        encounter.setPatientId("p1");
        encounter.setClinicianId("c1");
        encounter.setTranscript("Test");
        encounter.setTimestamp(OffsetDateTime.now());

        when(transcriptionService.transcribeEncounter(eq(tenantId), eq("p1"), eq("c1"), any())).thenReturn(encounter);

        mockMvc.perform(post("/api/encounters/transcribe")
                .header("X-Tenant-ID", tenantId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"patientId\":\"p1\", \"clinicianId\":\"c1\", \"audioBase64\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value("p1"))
                .andExpect(jsonPath("$.transcript").value("Test"));
    }

    @Test
    public void testGenerateNote() throws Exception {
        UUID tenantId = UUID.randomUUID();
        UUID encounterId = UUID.randomUUID();
        GeneratedNote note = new GeneratedNote();
        note.setId(UUID.randomUUID());
        note.setTenantId(tenantId);
        note.setEncounterId(encounterId);
        note.setContent("Generated SOAP note");
        note.setStatus(NoteStatus.DRAFT);
        note.setNoteType(NoteType.SOAP);

        when(noteGenerationService.generateNote(eq(tenantId), eq(encounterId), eq(NoteType.SOAP), eq("General"))).thenReturn(note);

        mockMvc.perform(post("/api/notes/generate")
                .header("X-Tenant-ID", tenantId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"encounterId\":\"" + encounterId + "\", \"noteType\":\"SOAP\", \"specialty\":\"General\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Generated SOAP note"));
    }

    @Test
    public void testSyncToEHR() throws Exception {
        UUID tenantId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        when(ehrIntegrationService.syncToEHR(eq(tenantId), eq(noteId), eq("Epic"))).thenReturn(true);

        mockMvc.perform(post("/api/ehr/epic/sync")
                .header("X-Tenant-ID", tenantId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"noteId\":\"" + noteId + "\", \"ehrSystem\":\"Epic\"}"))
                .andExpect(status().isOk());
    }
}
