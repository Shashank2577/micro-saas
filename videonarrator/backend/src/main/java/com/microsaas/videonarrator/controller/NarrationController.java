package com.microsaas.videonarrator.controller;

import com.microsaas.videonarrator.dto.NarrationRequest;
import com.microsaas.videonarrator.model.NarrationTrack;
import com.microsaas.videonarrator.service.NarrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Narrations")
public class NarrationController {

    private final NarrationService service;

    @GetMapping("/voices")
    @Operation(summary = "Get available narration voices")
    public ResponseEntity<List<Map<String, String>>> getVoices() {
        return ResponseEntity.ok(service.getAvailableVoices());
    }

    @PostMapping("/projects/{projectId}/narrate")
    @Operation(summary = "Start narration generation")
    public ResponseEntity<NarrationTrack> narrate(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID projectId,
            @RequestBody NarrationRequest request) {
        return ResponseEntity.ok(service.startNarration(
                tenantId, projectId, request.getVoiceProvider(), request.getVoiceId(), request.getTranscriptionId()));
    }

    @GetMapping("/projects/{projectId}/narrations")
    @Operation(summary = "Get narrations for a project")
    public ResponseEntity<List<NarrationTrack>> getNarrations(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID projectId) {
        return ResponseEntity.ok(service.getNarrations(tenantId, projectId));
    }
}
