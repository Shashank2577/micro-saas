package com.microsaas.jobcraftai.controller;

import com.microsaas.jobcraftai.model.*;
import com.microsaas.jobcraftai.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    public record GenerateRequest(String title, String department, String roleLevel, Map<String, Object> requirements) {}

    @PostMapping("/generate")
    public ResponseEntity<JobDescription> generate(@RequestBody GenerateRequest request) {
        return ResponseEntity.ok(jobService.generateJobDescription(request.title(), request.department(), request.roleLevel(), request.requirements()));
    }

    @GetMapping
    public ResponseEntity<List<JobDescription>> getAll() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDescription> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PostMapping("/{id}/check-bias")
    public ResponseEntity<BiasFlag> checkBias(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.checkBias(id));
    }

    public record VariantRequest(String variantName) {}

    @PostMapping("/{id}/variants")
    public ResponseEntity<JobVariant> createVariant(@PathVariable UUID id, @RequestBody VariantRequest request) {
        return ResponseEntity.ok(jobService.createVariant(id, request.variantName()));
    }

    @GetMapping("/{id}/performance")
    public ResponseEntity<Map<String, Object>> getPerformance(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.getPerformance(id));
    }

    public record OutcomeRequest(UUID candidateId, HireOutcome.Outcome outcome, String sourceChannel) {}

    @PostMapping("/{id}/outcomes")
    public ResponseEntity<HireOutcome> recordOutcome(@PathVariable UUID id, @RequestBody OutcomeRequest request) {
        return ResponseEntity.ok(jobService.recordOutcome(id, request.candidateId(), request.outcome(), request.sourceChannel()));
    }
}
