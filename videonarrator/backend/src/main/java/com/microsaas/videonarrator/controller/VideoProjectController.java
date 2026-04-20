package com.microsaas.videonarrator.controller;

import com.microsaas.videonarrator.domain.VideoProject;
import com.microsaas.videonarrator.service.VideoProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.crosscutting.starter.tenancy.TenantContext;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class VideoProjectController {

    private final VideoProcessingService videoProcessingService;

    @PostMapping
    public ResponseEntity<VideoProject> createProject(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title) throws Exception {
        String tenantId = TenantContext.require().toString();
        VideoProject project = videoProcessingService.uploadVideo(tenantId, title, file);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<VideoProject>> getProjects() {
        String tenantId = TenantContext.require().toString();
        return ResponseEntity.ok(videoProcessingService.getProjects(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoProject> getProject(@PathVariable UUID id) {
        String tenantId = TenantContext.require().toString();
        return ResponseEntity.ok(videoProcessingService.getProject(id, tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        String tenantId = TenantContext.require().toString();
        videoProcessingService.deleteProject(id, tenantId);
        return ResponseEntity.ok().build();
    }
}
