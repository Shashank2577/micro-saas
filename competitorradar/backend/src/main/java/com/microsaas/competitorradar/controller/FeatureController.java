package com.microsaas.competitorradar.controller;

import com.microsaas.competitorradar.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureService featureService;

    @GetMapping("/matrix")
    public ResponseEntity<Map<String, Object>> getFeatureMatrix() {
        return ResponseEntity.ok(featureService.getFeatureMatrix());
    }
}
