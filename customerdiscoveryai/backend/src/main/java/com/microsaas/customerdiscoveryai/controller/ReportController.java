package com.microsaas.customerdiscoveryai.controller;

import com.microsaas.customerdiscoveryai.model.Report;
import com.microsaas.customerdiscoveryai.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ProjectService projectService;

    public ReportController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    public Report getReport(@PathVariable UUID id) {
        return projectService.getReport(id);
    }
}
