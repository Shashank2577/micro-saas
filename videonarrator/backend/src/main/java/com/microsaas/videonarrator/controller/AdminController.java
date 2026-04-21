package com.microsaas.videonarrator.controller;

import com.microsaas.videonarrator.model.VideoProject;
import com.microsaas.videonarrator.service.VideoProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Video Projects")
public class AdminController {

    private final VideoProcessingService service;

    @GetMapping("/projects")
    @Operation(summary = "Get all video projects for tenant")
    public ResponseEntity<List<VideoProject>> getProjects(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.getProjects(tenantId));
    }
}
