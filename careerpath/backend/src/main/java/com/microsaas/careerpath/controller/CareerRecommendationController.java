package com.microsaas.careerpath.controller;

import com.microsaas.careerpath.service.CareerPathService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@Tag(name = "Career Recommendations API", description = "Endpoints for career recommendations")
public class CareerRecommendationController {

    private final CareerPathService careerPathService;

    @PostMapping("/employees/{id}/roles")
    @Operation(summary = "Generate role recommendations")
    public ResponseEntity<Map<String, String>> recommendRoles(@PathVariable UUID id) {
        String result = careerPathService.generateRoleRecommendations(id);
        return ResponseEntity.ok(Map.of("recommendations", result));
    }

    @PostMapping("/employees/{id}/learning-paths")
    @Operation(summary = "Generate learning paths")
    public ResponseEntity<Map<String, String>> generateLearningPaths(@PathVariable UUID id) {
        String result = careerPathService.generateLearningPaths(id);
        return ResponseEntity.ok(Map.of("learningPaths", result));
    }

    @PostMapping("/mentors/match")
    @Operation(summary = "Recommend mentors")
    public ResponseEntity<Map<String, String>> matchMentor(@RequestBody Map<String, String> request) {
        String goals = request.getOrDefault("careerGoals", "");
        String result = careerPathService.generateMentorMatch(goals);
        return ResponseEntity.ok(Map.of("match", result));
    }

    @PostMapping("/employees/{id}/assess-promotion")
    @Operation(summary = "Assess promotion readiness")
    public ResponseEntity<Map<String, String>> assessPromotion(@PathVariable UUID id) {
        String result = careerPathService.assessPromotionReadiness(id);
        return ResponseEntity.ok(Map.of("assessment", result));
    }

    @PostMapping("/managers/{id}/coaching-guidance")
    @Operation(summary = "Get coaching guidance")
    public ResponseEntity<Map<String, String>> getCoachingGuidance(@PathVariable UUID id, @RequestParam UUID employeeId) {
        String result = careerPathService.getCoachingGuidance(id, employeeId);
        return ResponseEntity.ok(Map.of("guidance", result));
    }
}
