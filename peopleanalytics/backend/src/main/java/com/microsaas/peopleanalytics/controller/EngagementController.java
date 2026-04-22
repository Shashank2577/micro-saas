package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.EngagementScore;
import com.microsaas.peopleanalytics.model.PulseSurvey;
import com.microsaas.peopleanalytics.service.EngagementScoringService;
import com.microsaas.peopleanalytics.service.PulseSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/people-analytics/engagement")
@RequiredArgsConstructor
public class EngagementController {
    private final EngagementScoringService engagementScoringService;
    private final PulseSurveyService pulseSurveyService;

    @GetMapping("/scores")
    public List<EngagementScore> getScores() {
        return engagementScoringService.getScores();
    }

    @GetMapping("/surveys")
    public List<PulseSurvey> getSurveys() {
        return pulseSurveyService.getAllSurveys();
    }

    @PostMapping("/surveys")
    public PulseSurvey createSurvey(@RequestBody PulseSurvey survey) {
        return pulseSurveyService.createSurvey(survey.getTitle(), survey.getDescription());
    }

    @PostMapping("/surveys/{id}/respond")
    public ResponseEntity<Void> respond(@PathVariable UUID id, @RequestBody SurveyResponseRequest request) {
        pulseSurveyService.submitResponse(id, request.employeeId(), request.score(), request.feedback());
        return ResponseEntity.ok().build();
    }

    public record SurveyResponseRequest(UUID employeeId, Integer score, String feedback) {}
}
