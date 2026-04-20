package com.microsaas.supportintelligence.controller;

import com.microsaas.supportintelligence.model.TicketPattern;
import com.microsaas.supportintelligence.service.PatternMiningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/insights")
@RequiredArgsConstructor
@Tag(name = "Insights", description = "Insights and patterns API")
public class InsightController {

    private final PatternMiningService patternService;

    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    @Operation(summary = "Get all ticket patterns")
    @GetMapping("/patterns")
    public ResponseEntity<List<TicketPattern>> getAllPatterns() {
        return ResponseEntity.ok(patternService.getAllPatterns(getTenantId()));
    }

    @Operation(summary = "Trigger pattern mining")
    @PostMapping("/patterns/mine")
    public ResponseEntity<Void> minePatterns() {
        patternService.minePatterns(getTenantId());
        return ResponseEntity.ok().build();
    }
}
