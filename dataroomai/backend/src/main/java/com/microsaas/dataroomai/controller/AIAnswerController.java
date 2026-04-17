package com.microsaas.dataroomai.controller;

import com.microsaas.dataroomai.domain.AIAnswer;
import com.microsaas.dataroomai.service.AIAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/data-rooms/{roomId}/ai-answers")
@RequiredArgsConstructor
public class AIAnswerController {

    private final AIAnswerService aiAnswerService;

    @GetMapping
    public ResponseEntity<List<AIAnswer>> getAnswers(
            @PathVariable UUID roomId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(aiAnswerService.getAIAnswers(roomId, tenantId));
    }

    @PostMapping
    public ResponseEntity<AIAnswer> createAnswer(
            @RequestBody AIAnswer aiAnswer,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        aiAnswer.setTenantId(tenantId);
        return ResponseEntity.ok(aiAnswerService.createAIAnswer(aiAnswer));
    }
}
