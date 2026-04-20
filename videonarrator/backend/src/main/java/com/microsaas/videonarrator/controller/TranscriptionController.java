package com.microsaas.videonarrator.controller;

import com.microsaas.videonarrator.domain.SubtitleTrack;
import com.microsaas.videonarrator.domain.Transcription;
import com.microsaas.videonarrator.dto.SubtitleUpdateRequest;
import com.microsaas.videonarrator.dto.TranscribeRequest;
import com.microsaas.videonarrator.service.TranscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.crosscutting.starter.tenancy.TenantContext;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TranscriptionController {

    private final TranscriptionService transcriptionService;

    @PostMapping("/projects/{projectId}/transcribe")
    public ResponseEntity<Transcription> transcribe(
            @PathVariable UUID projectId,
            @RequestBody TranscribeRequest request) {
        String tenantId = TenantContext.require().toString();
        Transcription transcription = transcriptionService.startTranscription(projectId, tenantId, request.getLanguageCode());
        return ResponseEntity.ok(transcription);
    }

    @GetMapping("/projects/{projectId}/transcriptions")
    public ResponseEntity<List<Transcription>> getTranscriptions(@PathVariable UUID projectId) {
        String tenantId = TenantContext.require().toString();
        return ResponseEntity.ok(transcriptionService.getTranscriptions(projectId, tenantId));
    }

    @GetMapping("/transcriptions/{transcriptionId}/subtitles")
    public ResponseEntity<List<SubtitleTrack>> getSubtitles(@PathVariable UUID transcriptionId) {
        String tenantId = TenantContext.require().toString();
        return ResponseEntity.ok(transcriptionService.getSubtitles(transcriptionId, tenantId));
    }

    @PutMapping("/subtitles/{subtitleId}")
    public ResponseEntity<SubtitleTrack> updateSubtitle(
            @PathVariable UUID subtitleId,
            @RequestBody SubtitleUpdateRequest request) {
        String tenantId = TenantContext.require().toString();
        SubtitleTrack track = transcriptionService.updateSubtitle(subtitleId, tenantId, request);
        return ResponseEntity.ok(track);
    }
}
