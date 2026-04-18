package com.microsaas.nonprofitos.controller;

import com.microsaas.nonprofitos.domain.Impact;
import com.microsaas.nonprofitos.dto.ImpactDto;
import com.microsaas.nonprofitos.service.ImpactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/impacts")
public class ImpactController {

    private final ImpactService impactService;

    public ImpactController(ImpactService impactService) {
        this.impactService = impactService;
    }

    @GetMapping
    public ResponseEntity<List<Impact>> getAllImpacts() {
        return ResponseEntity.ok(impactService.getAllImpacts());
    }

    @PostMapping
    public ResponseEntity<Impact> createImpact(@RequestBody ImpactDto dto) {
        return ResponseEntity.ok(impactService.createImpact(dto));
    }

    @PostMapping("/generate-narrative")
    public ResponseEntity<String> generateNarrative() {
        return ResponseEntity.ok(impactService.generateNarrative());
    }
}
