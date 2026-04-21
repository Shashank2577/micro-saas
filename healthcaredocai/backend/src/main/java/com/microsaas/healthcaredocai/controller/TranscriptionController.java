package com.microsaas.healthcaredocai.controller;

import com.microsaas.healthcaredocai.domain.ClinicalEncounter;
import com.microsaas.healthcaredocai.service.TranscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transcriptions")
@RequiredArgsConstructor
public class TranscriptionController {

    private final TranscriptionService transcriptionService;

    @PostMapping("/transcribe")
    public ResponseEntity<ClinicalEncounter> transcribeEncounter(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam String patientId,
            @RequestParam String clinicianId,
            @RequestBody byte[] audioData) {
        ClinicalEncounter encounter = transcriptionService.transcribeEncounter(tenantId, patientId, clinicianId, audioData);
        return ResponseEntity.ok(encounter);
    }
}
