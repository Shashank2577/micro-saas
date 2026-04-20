package com.microsaas.revopsai.controller;

import com.microsaas.revopsai.service.NlpRevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/nlp")
@RequiredArgsConstructor
public class NlpRevenueController {
    private final NlpRevenueService nlpRevenueService;

    @PostMapping("/query")
    public ResponseEntity<Map<String, String>> analyzeRevenueQuery(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        String result = nlpRevenueService.analyzeRevenueQuery(query);
        return ResponseEntity.ok(Map.of("result", result));
    }
}
