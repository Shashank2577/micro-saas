package com.microsaas.retirementplus.controller;

import com.microsaas.retirementplus.dto.ProjectionResponseDto;
import com.microsaas.retirementplus.service.ProjectionService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projections")
public class ProjectionController {

    private final ProjectionService projectionService;

    public ProjectionController(ProjectionService projectionService) {
        this.projectionService = projectionService;
    }

    @PostMapping("/analyze/{userId}")
    public ResponseEntity<ProjectionResponseDto> generateProjection(@PathVariable UUID userId) {
        UUID tenantId = TenantContext.require();
        ProjectionResponseDto response = projectionService.generateProjection(userId, tenantId);
        return ResponseEntity.ok(response);
    }
}
