package com.microsaas.usageintelligence.controller;

import com.microsaas.usageintelligence.service.UsageEventEmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usage-event-emitter")
@RequiredArgsConstructor
public class UsageEventEmitterController {

    private final UsageEventEmitter usageEventEmitter;

    @PostMapping("/emit-anomaly-detected")
    public ResponseEntity<Void> emitAnomalyDetected(@RequestParam String title, @RequestParam String recommendation) {
        usageEventEmitter.emitAnomalyDetected(title, recommendation);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/emit-cohort-created")
    public ResponseEntity<Void> emitCohortCreated(@RequestParam String cohortName) {
        usageEventEmitter.emitCohortCreated(cohortName);
        return ResponseEntity.ok().build();
    }
}
