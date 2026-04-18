package com.microsaas.videonarrator.controller;

import com.microsaas.videonarrator.dto.SubtitleUpdateRequest;
import com.microsaas.videonarrator.dto.TranscriptionRequest;
import com.microsaas.videonarrator.model.SubtitleTrack;
import com.microsaas.videonarrator.model.Transcription;
import com.microsaas.videonarrator.service.TranscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Transcriptions & Subtitles")
public class TranscriptionController {

    private final TranscriptionService service;

    @PostMapping("/projects/{projectId}/transcribe")
    @Operation(summary = "Start video transcription")
    public ResponseEntity<Transcription> transcribe(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID projectId,
            @RequestBody TranscriptionRequest request) {
        return ResponseEntity.ok(service.startTranscription(tenantId, projectId, request.getLanguageCode()));
    }

    @GetMapping("/projects/{projectId}/transcriptions")
    @Operation(summary = "Get transcriptions for a project")
    public ResponseEntity<List<Transcription>> getTranscriptions(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID projectId) {
        return ResponseEntity.ok(service.getTranscriptions(tenantId, projectId));
    }

    @GetMapping("/transcriptions/{transcriptionId}/subtitles")
    @Operation(summary = "Get subtitles for a transcription")
    public ResponseEntity<List<SubtitleTrack>> getSubtitles(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID transcriptionId) {
        return ResponseEntity.ok(service.getSubtitles(tenantId, transcriptionId));
    }

    @PutMapping("/subtitles/{subtitleId}")
    @Operation(summary = "Update a subtitle track")
    public ResponseEntity<SubtitleTrack> updateSubtitle(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID subtitleId,
            @RequestBody SubtitleUpdateRequest request) {
        return ResponseEntity.ok(service.updateSubtitle(
                tenantId, subtitleId, request.getStartTimeMs(), request.getEndTimeMs(), request.getContent()));
    }
}
