package com.microsaas.careerpath.controller;

import com.microsaas.careerpath.dto.*;
import com.microsaas.careerpath.service.CareerPathService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "CareerPath API", description = "Endpoints for career progression planning")
public class CareerPathController {

    @Autowired
    private CareerPathService careerPathService;

    @GetMapping("/roles")
    @Operation(summary = "Get all roles")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(careerPathService.getAllRoles());
    }

    @GetMapping("/roles/{id}")
    @Operation(summary = "Get role details including required skills")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable UUID id) {
        return ResponseEntity.ok(careerPathService.getRoleById(id));
    }

    @GetMapping("/roadmaps")
    @Operation(summary = "Get career roadmap")
    public ResponseEntity<RoadmapDto> getRoadmap() {
        return ResponseEntity.ok(careerPathService.getRoadmap());
    }

    @GetMapping("/employees/{id}/skills")
    @Operation(summary = "Get employee skill inventory")
    public ResponseEntity<List<EmployeeSkillDto>> getEmployeeSkills(@PathVariable UUID id) {
        return ResponseEntity.ok(careerPathService.getEmployeeSkills(id));
    }

    @PostMapping("/employees/{id}/skills")
    @Operation(summary = "Update employee skill")
    public ResponseEntity<Void> updateEmployeeSkill(@PathVariable UUID id, @RequestBody SkillUpdateDto dto) {
        careerPathService.updateEmployeeSkill(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/employees/{id}/skill-gaps")
    @Operation(summary = "Calculate skill gaps")
    public ResponseEntity<List<SkillGapDto>> getSkillGaps(@PathVariable UUID id, @RequestParam UUID targetRoleId) {
        return ResponseEntity.ok(careerPathService.calculateSkillGaps(id, targetRoleId));
    }

    @PostMapping("/employees/{id}/recommend-roles")
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
