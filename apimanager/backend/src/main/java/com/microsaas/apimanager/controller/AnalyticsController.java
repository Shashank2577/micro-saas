package com.microsaas.apimanager.controller;

import com.microsaas.apimanager.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/projects/{id}/summary")
    public ResponseEntity<Map<String, Object>> getSummary(@PathVariable UUID id) {
        return ResponseEntity.ok(analyticsService.getSummary(id));
    }
}
