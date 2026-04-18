package com.microsaas.customerdiscoveryai.controller;

import com.microsaas.customerdiscoveryai.model.Interview;
import com.microsaas.customerdiscoveryai.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final ProjectService projectService;

    public InterviewController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/{id}/submit-transcript")
    public Interview submitTranscript(@PathVariable UUID id, @RequestBody Map<String, String> payload) {
        return projectService.submitTranscript(id, payload.get("transcript"));
    }
}
