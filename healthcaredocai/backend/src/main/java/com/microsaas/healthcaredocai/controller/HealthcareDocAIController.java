package com.microsaas.healthcaredocai.controller;

import com.microsaas.healthcaredocai.domain.ClinicalEncounter;
import com.microsaas.healthcaredocai.domain.ClinicalTemplate;
import com.microsaas.healthcaredocai.domain.GeneratedNote;
import com.microsaas.healthcaredocai.domain.NoteType;
import com.microsaas.healthcaredocai.dto.EHRSyncRequest;
import com.microsaas.healthcaredocai.dto.GenerateNoteRequest;
import com.microsaas.healthcaredocai.dto.TranscribeRequest;
import com.microsaas.healthcaredocai.repository.ClinicalTemplateRepository;
import com.microsaas.healthcaredocai.service.EHRIntegrationService;
import com.microsaas.healthcaredocai.service.NoteGenerationService;
import com.microsaas.healthcaredocai.service.TranscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class HealthcareDocAIController {

    private final TranscriptionService transcriptionService;
    private final NoteGenerationService noteGenerationService;
    private final EHRIntegrationService ehrIntegrationService;
    private final ClinicalTemplateRepository clinicalTemplateRepository;

    public HealthcareDocAIController(TranscriptionService transcriptionService,
                                     NoteGenerationService noteGenerationService,
                                     EHRIntegrationService ehrIntegrationService,
                                     ClinicalTemplateRepository clinicalTemplateRepository) {
        this.transcriptionService = transcriptionService;
        this.noteGenerationService = noteGenerationService;
        this.ehrIntegrationService = ehrIntegrationService;
        this.clinicalTemplateRepository = clinicalTemplateRepository;
    }

    @PostMapping("/encounters/transcribe")
    public ResponseEntity<ClinicalEncounter> transcribe(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                                        @RequestBody TranscribeRequest request) {
        byte[] audioData = request.getAudioBase64() != null ? request.getAudioBase64().getBytes() : new byte[0];
        ClinicalEncounter encounter = transcriptionService.transcribeEncounter(tenantId, request.getPatientId(), request.getClinicianId(), audioData);
        return ResponseEntity.ok(encounter);
    }

    @PostMapping("/notes/generate")
    public ResponseEntity<GeneratedNote> generateNote(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                                      @RequestBody GenerateNoteRequest request) {
        GeneratedNote note = noteGenerationService.generateNote(tenantId, request.getEncounterId(), request.getNoteType(), request.getSpecialty());
        return ResponseEntity.ok(note);
    }

    @GetMapping("/templates/{specialty}")
    public ResponseEntity<List<ClinicalTemplate>> getTemplates(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                                               @PathVariable String specialty) {
        return ResponseEntity.ok(clinicalTemplateRepository.findByTenantIdAndSpecialty(tenantId, specialty));
    }

    @PostMapping("/ehr/epic/sync")
    public ResponseEntity<String> syncToEpicEHR(@RequestHeader("X-Tenant-ID") UUID tenantId,
                                                @RequestBody EHRSyncRequest request) {
        boolean success = ehrIntegrationService.syncToEHR(tenantId, request.getNoteId(), "Epic");
        if (success) {
            return ResponseEntity.ok("Successfully synced to Epic");
        } else {
            return ResponseEntity.badRequest().body("Failed to sync to Epic");
        }
    }
}
