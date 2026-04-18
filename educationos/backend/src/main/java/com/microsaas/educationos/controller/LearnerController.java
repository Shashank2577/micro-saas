package com.microsaas.educationos.controller;

import com.microsaas.educationos.dto.LearnerProfileDto;
import com.microsaas.educationos.service.LearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/learners")
public class LearnerController {

    @Autowired
    private LearnerService learnerService;

    @GetMapping("/{userId}")
    public ResponseEntity<LearnerProfileDto> getProfile(@PathVariable UUID userId) {
        LearnerProfileDto profile = learnerService.getLearnerProfile(userId);
        return profile != null ? ResponseEntity.ok(profile) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<LearnerProfileDto> createOrUpdateProfile(@RequestBody LearnerProfileDto dto) {
        return ResponseEntity.ok(learnerService.createOrUpdateLearnerProfile(dto));
    }
}
