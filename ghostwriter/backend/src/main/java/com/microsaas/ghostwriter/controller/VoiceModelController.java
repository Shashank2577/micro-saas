package com.microsaas.ghostwriter.controller;

import com.microsaas.ghostwriter.model.CorpusItem;
import com.microsaas.ghostwriter.model.VoiceModel;
import com.microsaas.ghostwriter.service.VoiceModelService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/voice-models")
@RequiredArgsConstructor
public class VoiceModelController {

    private final VoiceModelService voiceModelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoiceModel createVoiceModel(
            @RequestBody CreateVoiceModelRequest request,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return voiceModelService.createVoiceModel(
                request.getName(),
                request.getOwnerId() != null ? request.getOwnerId() : tenantId, // Using tenantId as fallback for ownerId
                tenantId
        );
    }

    @PostMapping("/{id}/corpus")
    @ResponseStatus(HttpStatus.CREATED)
    public CorpusItem addCorpusContent(
            @PathVariable UUID id,
            @RequestBody AddCorpusRequest request,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return voiceModelService.addCorpusContent(
                id,
                request.getTitle(),
                request.getContent(),
                tenantId
        );
    }

    @PostMapping("/{id}/train")
    public VoiceModel trainVoiceModel(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return voiceModelService.trainModel(id, tenantId);
    }

    @Data
    public static class CreateVoiceModelRequest {
        private String name;
        private UUID ownerId;
    }

    @Data
    public static class AddCorpusRequest {
        private String title;
        private String content;
    }
}
