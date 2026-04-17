package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.PulseSurvey;
import com.microsaas.peopleanalytics.model.SurveyResponse;
import com.microsaas.peopleanalytics.service.PulseSurveyService;
import com.microsaas.peopleanalytics.dto.SurveyDto;
import com.microsaas.peopleanalytics.dto.SurveyResponseDto;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final PulseSurveyService service;

    @GetMapping
    public ResponseEntity<List<PulseSurvey>> getSurveys(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.getSurveys(tenantId));
    }

    @PostMapping
    public ResponseEntity<PulseSurvey> createSurvey(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody SurveyDto dto) {
        return ResponseEntity.ok(service.createSurvey(tenantId, dto));
    }

    @PostMapping("/{id}/distribute")
    public ResponseEntity<PulseSurvey> distributeSurvey(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.distributeSurvey(tenantId, id));
    }

    @PostMapping("/{id}/responses")
    public ResponseEntity<SurveyResponse> submitResponse(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id,
            @RequestBody SurveyResponseDto dto) {
        return ResponseEntity.ok(service.submitResponse(tenantId, id, dto));
    }

    @GetMapping("/{id}/analysis")
    public ResponseEntity<Map<String, Object>> analyzeSurvey(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.analyzeSurvey(tenantId, id));
    }
}
