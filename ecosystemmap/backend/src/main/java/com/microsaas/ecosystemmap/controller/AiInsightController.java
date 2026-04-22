package com.microsaas.ecosystemmap.controller;

import com.microsaas.ecosystemmap.entity.AiInsight;
import com.microsaas.ecosystemmap.service.AiInsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ecosystems/{ecosystemId}/insights")
@RequiredArgsConstructor
public class AiInsightController {

    private final AiInsightService aiInsightService;

    @GetMapping
    public ResponseEntity<List<AiInsight>> getInsightsByEcosystem(@PathVariable UUID ecosystemId) {
        return ResponseEntity.ok(aiInsightService.getInsightsByEcosystem(ecosystemId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiInsight> getInsightById(@PathVariable UUID ecosystemId, @PathVariable UUID id) {
        return ResponseEntity.ok(aiInsightService.getInsightById(id));
    }

    @PostMapping("/generate")
    public ResponseEntity<AiInsight> generateInsight(
            @PathVariable UUID ecosystemId,
            @RequestParam String insightType) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(aiInsightService.generateInsight(ecosystemId, insightType));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AiInsight> updateInsightStatus(
            @PathVariable UUID ecosystemId,
            @PathVariable UUID id,
            @RequestParam String status) {
        return ResponseEntity.ok(aiInsightService.updateInsightStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsight(@PathVariable UUID ecosystemId, @PathVariable UUID id) {
        aiInsightService.deleteInsight(id);
        return ResponseEntity.noContent().build();
    }
}
