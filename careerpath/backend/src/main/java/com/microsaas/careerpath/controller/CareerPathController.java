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

}
