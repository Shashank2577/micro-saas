package com.microsaas.localizationos.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.localizationos.domain.TranslationJob;
import com.microsaas.localizationos.domain.TranslationProject;
import com.microsaas.localizationos.domain.CulturalFlag;
import com.microsaas.localizationos.dto.QualityMetrics;
import com.microsaas.localizationos.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/translations")
public class TranslationController {

    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping("/projects")
    public ResponseEntity<TranslationProject> createProject(
            @RequestParam String name,
            @RequestParam String sourceLanguage,
            @RequestParam List<String> targetLanguages) {
        return ResponseEntity.ok(translationService.createProject(name, sourceLanguage, targetLanguages, TenantContext.require()));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<TranslationProject>> listProjects() {
        return ResponseEntity.ok(translationService.listProjects(TenantContext.require()));
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<TranslationProject> getProject(@PathVariable UUID projectId) {
        return ResponseEntity.ok(translationService.getProject(projectId, TenantContext.require()));
    }

    @PostMapping("/projects/{projectId}/jobs")
    public ResponseEntity<TranslationJob> submitTranslationJob(
            @PathVariable UUID projectId,
            @RequestParam String sourceContent,
            @RequestParam String sourceLanguage,
            @RequestParam String targetLanguage) {
        return ResponseEntity.ok(translationService.submitTranslationJob(projectId, sourceContent, sourceLanguage, targetLanguage, TenantContext.require()));
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<TranslationJob> getJob(@PathVariable UUID jobId) {
        return ResponseEntity.ok(translationService.getJob(jobId, TenantContext.require()));
    }

    @PostMapping("/jobs/{jobId}/approve")
    public ResponseEntity<TranslationJob> approveJob(@PathVariable UUID jobId) {
        return ResponseEntity.ok(translationService.approveJob(jobId, TenantContext.require()));
    }

    @GetMapping("/jobs/{jobId}/flags")
    public ResponseEntity<List<CulturalFlag>> getJobFlags(@PathVariable UUID jobId) {
        return ResponseEntity.ok(translationService.getJobFlags(jobId, TenantContext.require()));
    }

    @GetMapping("/projects/{projectId}/metrics")
    public ResponseEntity<QualityMetrics> getQualityMetrics(@PathVariable UUID projectId) {
        return ResponseEntity.ok(translationService.getQualityMetrics(projectId, TenantContext.require()));
    }
}
