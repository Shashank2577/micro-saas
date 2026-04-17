package com.microsaas.videonarrator.controller;

import com.microsaas.videonarrator.model.*;
import com.microsaas.videonarrator.service.VideoNarratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoNarratorService videoNarratorService;

    @PostMapping
    public ResponseEntity<VideoAsset> registerVideo(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody RegisterVideoRequest request) {
        VideoAsset asset = videoNarratorService.registerVideo(
                request.getTitle(),
                request.getFileUrl(),
                request.getDurationSeconds(),
                tenantId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping
    public ResponseEntity<List<VideoAsset>> getVideos(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(videoNarratorService.getVideos(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoAsset> getVideo(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return videoNarratorService.getVideo(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/transcribe")
    public ResponseEntity<Transcript> transcribe(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(videoNarratorService.generateTranscript(id, tenantId));
    }

    @GetMapping("/{id}/transcript")
    public ResponseEntity<Transcript> getTranscript(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return videoNarratorService.getTranscript(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/chapters")
    public ResponseEntity<List<VideoChapter>> generateChapters(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(videoNarratorService.generateChapters(id, tenantId));
    }

    @PostMapping("/{id}/derive")
    public ResponseEntity<List<DerivedContent>> deriveContent(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(videoNarratorService.generateDerivedContent(id, tenantId));
    }

    @GetMapping("/{id}/clips")
    public ResponseEntity<List<VideoClip>> getClips(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        // Here we first generate or just get. The prompt says GET /api/v1/videos/{id}/clips — get best short clips
        // We will just return the list, but we should generate them if they don't exist
        List<VideoClip> clips = videoNarratorService.getClips(id, tenantId);
        if (clips.isEmpty()) {
            clips = videoNarratorService.generateClips(id, tenantId);
        }
        return ResponseEntity.ok(clips);
    }
}
