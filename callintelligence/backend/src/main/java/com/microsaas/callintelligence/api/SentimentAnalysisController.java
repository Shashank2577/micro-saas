package com.microsaas.callintelligence.api;

import com.microsaas.callintelligence.domain.insight.SentimentAnalysis;
import com.microsaas.callintelligence.service.SentimentAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sentiment")
public class SentimentAnalysisController {

    private final SentimentAnalysisService sentimentAnalysisService;

    public SentimentAnalysisController(SentimentAnalysisService sentimentAnalysisService) {
        this.sentimentAnalysisService = sentimentAnalysisService;
    }

    @GetMapping("/call/{callId}")
    public ResponseEntity<List<SentimentAnalysis>> getSentimentByCall(@PathVariable UUID callId) {
        return ResponseEntity.ok(sentimentAnalysisService.getSentimentForCall(callId));
    }
}
