package com.microsaas.datagovernanceos.controller;

import com.microsaas.datagovernanceos.dto.DataClassificationResult;
import com.microsaas.datagovernanceos.entity.DataAsset;
import com.microsaas.datagovernanceos.service.DataClassificationAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/data-classification")
@RequiredArgsConstructor
public class DataClassificationAiController {

    private final DataClassificationAiService dataClassificationAiService;

    @PostMapping("/classify")
    public ResponseEntity<DataClassificationResult> classifyAsset(@RequestBody DataAsset asset) {
        return ResponseEntity.ok(dataClassificationAiService.classifyAsset(asset));
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeText(@RequestBody Map<String, Object> requestData) {
        return ResponseEntity.ok(dataClassificationAiService.analyzeText(requestData));
    }
}
