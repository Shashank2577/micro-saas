package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.TeamHealthMetric;
import com.microsaas.peopleanalytics.service.TeamHealthAnalysisService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/team-health")
@RequiredArgsConstructor
public class TeamHealthController {

    private final TeamHealthAnalysisService service;

    @GetMapping
    public ResponseEntity<List<TeamHealthMetric>> getMetrics(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
