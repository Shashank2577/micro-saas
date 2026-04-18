package com.microsaas.processminer.controller;

import com.microsaas.processminer.domain.AutomationOpportunity;
import com.microsaas.processminer.service.AnalysisService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/automation-opportunities")
public class AutomationOpportunityController {

    private final AnalysisService analysisService;

    public AutomationOpportunityController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping
    public List<AutomationOpportunity> getAll() {
        return analysisService.getAutomationOpportunities();
    }
}
