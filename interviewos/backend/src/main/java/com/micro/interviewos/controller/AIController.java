package com.micro.interviewos.controller;

import com.micro.interviewos.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyze(@RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(aiService.analyze(tenantId, request));
    }
}
