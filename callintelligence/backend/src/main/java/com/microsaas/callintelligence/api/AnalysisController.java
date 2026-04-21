package com.microsaas.callintelligence.api;

import com.microsaas.callintelligence.service.AnalysisService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/{callId}/analyze")
    public ResponseEntity<Void> analyzeCall(@PathVariable UUID callId) {
        UUID tenantId = TenantContext.require();
        analysisService.analyzeCallAsync(callId, tenantId);
        return ResponseEntity.accepted().build();
    }
}
