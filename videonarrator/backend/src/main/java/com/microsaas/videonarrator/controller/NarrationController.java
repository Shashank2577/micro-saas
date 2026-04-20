package com.microsaas.videonarrator.controller;

import com.microsaas.videonarrator.domain.NarrationTrack;
import com.microsaas.videonarrator.dto.NarrateRequest;
import com.microsaas.videonarrator.dto.VoiceDto;
import com.microsaas.videonarrator.service.NarrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.crosscutting.starter.tenancy.TenantContext;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NarrationController {

    private final NarrationService narrationService;

    @GetMapping("/voices")
    public ResponseEntity<List<VoiceDto>> getVoices() {
        return ResponseEntity.ok(narrationService.getVoices());
    }

    @PostMapping("/projects/{projectId}/narrate")
    public ResponseEntity<NarrationTrack> narrate(
            @PathVariable UUID projectId,
            @RequestBody NarrateRequest request) {
        String tenantId = TenantContext.require().toString();
        NarrationTrack track = narrationService.startNarration(projectId, tenantId, request);
        return ResponseEntity.ok(track);
    }
}
