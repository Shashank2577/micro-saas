package com.microsaas.callintelligence.api;

import com.microsaas.callintelligence.domain.call.*;
import com.microsaas.callintelligence.domain.insight.*;
import com.microsaas.callintelligence.service.CallService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/calls")
public class CallController {

    private final CallService callService;
    private final CallSpeakerRepository speakerRepository;
    private final CallTranscriptRepository transcriptRepository;
    private final CallInsightRepository insightRepository;

    public CallController(CallService callService, 
                          CallSpeakerRepository speakerRepository, 
                          CallTranscriptRepository transcriptRepository, 
                          CallInsightRepository insightRepository) {
        this.callService = callService;
        this.speakerRepository = speakerRepository;
        this.transcriptRepository = transcriptRepository;
        this.insightRepository = insightRepository;
    }

    @PostMapping
    public ResponseEntity<Call> uploadCall(@RequestBody Map<String, Object> request) {
        String title = (String) request.get("title");
        String repId = (String) request.get("repId");
        String audioUrl = (String) request.get("audioUrl");
        Integer duration = request.containsKey("durationSeconds") ? (Integer) request.get("durationSeconds") : 0;
        
        Call call = callService.createCall(title, repId, audioUrl, duration);
        callService.startAnalysis(call.getId());
        return ResponseEntity.ok(call);
    }

    @GetMapping
    public ResponseEntity<Page<Call>> listCalls(
            @RequestParam(required = false) String repId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(callService.getCalls(repId, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Call> getCall(@PathVariable UUID id) {
        return callService.getCall(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/speakers")
    public ResponseEntity<List<CallSpeaker>> getCallSpeakers(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(speakerRepository.findByCallIdAndTenantId(id, tenantId));
    }

    @GetMapping("/{id}/transcript")
    public ResponseEntity<List<CallTranscript>> getCallTranscript(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(transcriptRepository.findByCallIdAndTenantIdOrderByStartTimeAsc(id, tenantId));
    }

    @GetMapping("/{id}/insights")
    public ResponseEntity<List<CallInsight>> getCallInsights(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(insightRepository.findByCallIdAndTenantId(id, tenantId));
    }
}
