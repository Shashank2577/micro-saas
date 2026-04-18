package com.microsaas.videonarrator.controller;

import com.microsaas.videonarrator.model.VideoProject;
import com.microsaas.videonarrator.service.VideoProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Video Projects")
public class VideoProjectController {

    private final VideoProcessingService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a new video project")
    public ResponseEntity<VideoProject> uploadProject(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(service.uploadVideo(tenantId, title, file));
    }

    @GetMapping
    @Operation(summary = "Get all video projects for tenant")
    public ResponseEntity<List<VideoProject>> getProjects(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.getProjects(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get video project details")
    public ResponseEntity<VideoProject> getProject(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.getProject(tenantId, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete video project")
    public ResponseEntity<Void> deleteProject(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID id) {
        service.deleteProject(tenantId, id);
        return ResponseEntity.noContent().build();
    }
}
