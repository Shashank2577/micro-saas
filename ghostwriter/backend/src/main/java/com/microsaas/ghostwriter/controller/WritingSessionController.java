package com.microsaas.ghostwriter.controller;

import com.microsaas.ghostwriter.model.DraftSection;
import com.microsaas.ghostwriter.model.ResearchNote;
import com.microsaas.ghostwriter.model.WritingSession;
import com.microsaas.ghostwriter.service.WritingSessionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class WritingSessionController {

    private final WritingSessionService sessionService;

    @GetMapping
    public List<WritingSession> listSessions(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return sessionService.listSessions(tenantId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WritingSession startSession(
            @RequestBody StartSessionRequest request,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return sessionService.startSession(
                request.getTitle(),
                request.getTopic(),
                request.getTargetWordCount(),
                request.getVoiceModelId(),
                tenantId
        );
    }

    @PostMapping("/{id}/outline")
    public DraftSection generateOutline(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return sessionService.generateOutline(id, tenantId);
    }

    @PostMapping("/{id}/draft-section")
    public DraftSection draftSection(
            @PathVariable UUID id,
            @RequestBody DraftSectionRequest request,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return sessionService.draftSection(
                id,
                request.getHeading(),
                request.getSectionOrder(),
                tenantId
        );
    }

    @PostMapping("/{id}/research")
    public List<ResearchNote> researchTopic(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return sessionService.researchTopic(id, tenantId);
    }

    @Data
    public static class StartSessionRequest {
        private String title;
        private String topic;
        private Integer targetWordCount;
        private UUID voiceModelId;
    }

    @Data
    public static class DraftSectionRequest {
        private String heading;
        private Integer sectionOrder;
    }
}
