package com.microsaas.careerpath.controller;

import com.microsaas.careerpath.dto.SkillGapDto;
import com.microsaas.careerpath.service.CareerPathService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skill-gaps")
@RequiredArgsConstructor
@Tag(name = "Skill Gap API", description = "Endpoints for skill gap analysis")
public class SkillGapController {

    private final CareerPathService careerPathService;

    @GetMapping("/employees/{id}")
    @Operation(summary = "Calculate skill gaps")
    public ResponseEntity<List<SkillGapDto>> getSkillGaps(@PathVariable UUID id, @RequestParam UUID targetRoleId) {
        return ResponseEntity.ok(careerPathService.calculateSkillGaps(id, targetRoleId));
    }
}
