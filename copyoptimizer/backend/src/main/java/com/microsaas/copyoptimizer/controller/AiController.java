package com.microsaas.copyoptimizer.controller;

import com.microsaas.copyoptimizer.service.CopyAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/copy/ai")
@RequiredArgsConstructor
public class AiController {

    private final CopyAiService aiService;

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyze(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(aiService.analyze(request));
    }

    @PostMapping("/recommendations")
    public ResponseEntity<Map<String, Object>> recommendations(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(aiService.recommend(request));
    }
}
