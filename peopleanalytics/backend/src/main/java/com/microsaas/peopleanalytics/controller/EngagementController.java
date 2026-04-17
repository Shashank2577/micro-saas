package com.microsaas.peopleanalytics.controller;

import com.microsaas.peopleanalytics.model.EngagementScore;
import com.microsaas.peopleanalytics.service.EngagementScoringService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/engagement")
@RequiredArgsConstructor
public class EngagementController {

    private final EngagementScoringService service;

    @GetMapping("/trends")
    public ResponseEntity<List<EngagementScore>> getTrends(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.getTrends(tenantId));
    }

    @PostMapping("/calculate")
    public ResponseEntity<Void> calculateScores(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        service.calculateScores(tenantId);
        return ResponseEntity.ok().build();
    }
}
