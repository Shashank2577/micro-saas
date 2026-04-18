package com.microsaas.observabilityai;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ObservabilityController {

    private final ObservabilityService service;
    private final AIAnalysisService aiAnalysisService;

    public ObservabilityController(ObservabilityService service, AIAnalysisService aiAnalysisService) {
        this.service = service;
        this.aiAnalysisService = aiAnalysisService;
    }

    @PostMapping("/signals")
    public ResponseEntity<ObservabilitySignal> createSignal(@RequestBody ObservabilitySignal signal) {
        return ResponseEntity.ok(service.createSignal(signal));
    }

    @GetMapping("/signals")
    public ResponseEntity<List<ObservabilitySignal>> getSignals(@RequestParam(required = false) String type) {
        return ResponseEntity.ok(service.getSignals(type));
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<ObservabilityAlert>> getAlerts() {
        return ResponseEntity.ok(service.getAlerts());
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeTrace(@RequestBody Map<String, String> request) {
        String traceId = request.get("traceId");
        if (traceId == null || traceId.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"traceId is required\"}");
        }
        
        List<ObservabilitySignal> signals = service.getSignalsByTraceId(traceId);
        if (signals.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String analysis = aiAnalysisService.analyzeTrace(signals);
        return ResponseEntity.ok(analysis);
    }
}
