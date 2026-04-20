package com.microsaas.experimentengine.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.experimentengine.domain.model.Variant;
import com.microsaas.experimentengine.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/assign")
    public ResponseEntity<Variant> assignVariant(
            @RequestParam UUID experimentId,
            @RequestParam String unitId) {
        UUID tenantId = TenantContext.require();
        Variant assignedVariant = assignmentService.assign(experimentId, unitId, tenantId);
        return ResponseEntity.ok(assignedVariant);
    }
}
