package com.microsaas.experimentengine.controller;

import com.microsaas.experimentengine.service.HypothesisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hypotheses")
public class HypothesisController {

    private final HypothesisService hypothesisService;

    @Autowired
    public HypothesisController(HypothesisService hypothesisService) {
        this.hypothesisService = hypothesisService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateHypothesis(@RequestBody Map<String, String> request) {
        String problem = request.get("problem");
        String solution = request.get("solution");
        String hypothesis = hypothesisService.generateHypothesis(problem, solution);
        return ResponseEntity.ok(Map.of("hypothesis", hypothesis));
    }
}
