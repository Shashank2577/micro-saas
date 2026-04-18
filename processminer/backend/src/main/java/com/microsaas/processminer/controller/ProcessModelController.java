package com.microsaas.processminer.controller;

import com.microsaas.processminer.domain.ProcessModel;
import com.microsaas.processminer.domain.AnalysisResult;
import com.microsaas.processminer.dto.ProcessDiscoveryRequest;
import com.microsaas.processminer.service.ProcessDiscoveryService;
import com.microsaas.processminer.service.AnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/process-models")
public class ProcessModelController {

    private final ProcessDiscoveryService discoveryService;
    private final AnalysisService analysisService;

    public ProcessModelController(ProcessDiscoveryService discoveryService, AnalysisService analysisService) {
        this.discoveryService = discoveryService;
        this.analysisService = analysisService;
    }

    @PostMapping("/discover")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProcessModel discover(@RequestBody @Valid ProcessDiscoveryRequest request) {
        return discoveryService.discoverProcessModel(request.systemType());
    }

    @GetMapping
    public List<ProcessModel> getAll() {
        return discoveryService.getModels();
    }

    @GetMapping("/{id}")
    public ProcessModel getById(@PathVariable UUID id) {
        return discoveryService.getModel(id);
    }

    @PostMapping("/{id}/analyze")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void analyze(@PathVariable UUID id) {
        analysisService.analyzeProcessModel(id);
    }

    @GetMapping("/{id}/analysis")
    public List<AnalysisResult> getAnalysis(@PathVariable UUID id) {
        return analysisService.getAnalysisResults(id);
    }
}
