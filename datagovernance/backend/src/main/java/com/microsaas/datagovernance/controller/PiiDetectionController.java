package com.microsaas.datagovernance.controller;

import com.microsaas.datagovernance.dto.PiiDetectRequest;
import com.microsaas.datagovernance.service.PiiDetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pii")
@RequiredArgsConstructor
@Tag(name = "PII Detection API", description = "Endpoints for PII detection")
public class PiiDetectionController {

    private final PiiDetectionService piiService;

    @Operation(summary = "Detect PII in text using AI")
    @PostMapping("/detect")
    public ResponseEntity<?> detectPii(@RequestBody PiiDetectRequest request) {
        return ResponseEntity.ok(piiService.detectPii(request));
    }
}
