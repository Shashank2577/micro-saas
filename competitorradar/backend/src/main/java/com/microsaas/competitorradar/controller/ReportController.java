package com.microsaas.competitorradar.controller;

import com.microsaas.competitorradar.service.ReportGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportGeneratorService reportGeneratorService;

    @GetMapping("/quarterly")
    public ResponseEntity<Map<String, Object>> getQuarterlyReport() {
        return ResponseEntity.ok(reportGeneratorService.generateQuarterlyReport());
    }
}
