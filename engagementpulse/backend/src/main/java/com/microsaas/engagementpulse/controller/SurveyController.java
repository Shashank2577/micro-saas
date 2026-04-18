package com.microsaas.engagementpulse.controller;

import com.microsaas.engagementpulse.dto.SurveyDto;
import com.microsaas.engagementpulse.model.Survey;
import com.microsaas.engagementpulse.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Surveys", description = "Survey Management API")
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody SurveyDto dto) {
        return ResponseEntity.ok(surveyService.createSurvey(dto));
    }

    @GetMapping
    public ResponseEntity<List<Survey>> listSurveys() {
        return ResponseEntity.ok(surveyService.listSurveys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurvey(@PathVariable UUID id) {
        return ResponseEntity.ok(surveyService.getSurvey(id));
    }

    @PostMapping("/{id}/distribute")
    public ResponseEntity<Void> distributeSurvey(@PathVariable UUID id) {
        surveyService.distributeSurvey(id);
        return ResponseEntity.ok().build();
    }
}
