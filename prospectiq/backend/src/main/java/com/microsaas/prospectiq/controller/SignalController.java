package com.microsaas.prospectiq.controller;

import com.microsaas.prospectiq.dto.SignalRequest;
import com.microsaas.prospectiq.model.Signal;
import com.microsaas.prospectiq.service.SignalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Signals")
public class SignalController {

    private final SignalService signalService;

    @GetMapping("/prospects/{id}/signals")
    @Operation(summary = "Get signals for a prospect")
    public ResponseEntity<List<Signal>> getSignalsForProspect(@PathVariable UUID id) {
        return ResponseEntity.ok(signalService.getSignalsForProspect(id));
    }

    @PostMapping("/signals/ingest")
    @Operation(summary = "Ingest a new signal")
    public ResponseEntity<Signal> ingestSignal(@RequestBody SignalRequest request) {
        return ResponseEntity.ok(signalService.ingestSignal(request));
    }
}
