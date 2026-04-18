package com.microsaas.churnpredictor.controller;

import com.microsaas.churnpredictor.dto.InterventionDto;
import com.microsaas.churnpredictor.dto.InterventionPlaybookDto;
import com.microsaas.churnpredictor.service.InterventionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InterventionController {
    private final InterventionService interventionService;

    @GetMapping("/playbooks")
    public ResponseEntity<List<InterventionPlaybookDto>> getPlaybooks() {
        return ResponseEntity.ok(interventionService.getPlaybooks());
    }

    @PostMapping("/playbooks")
    public ResponseEntity<InterventionPlaybookDto> createPlaybook(@RequestBody InterventionPlaybookDto dto) {
        return ResponseEntity.ok(interventionService.createPlaybook(dto));
    }

    @GetMapping("/interventions")
    public ResponseEntity<List<InterventionDto>> getInterventions() {
        return ResponseEntity.ok(interventionService.getInterventions());
    }

    @PostMapping("/interventions/{id}/generate-offer")
    public ResponseEntity<InterventionDto> generateOffer(@PathVariable UUID id) {
        return ResponseEntity.ok(interventionService.generateOffer(id));
    }
}
