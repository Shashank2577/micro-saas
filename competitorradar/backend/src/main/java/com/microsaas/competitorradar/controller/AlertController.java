package com.microsaas.competitorradar.controller;

import com.microsaas.competitorradar.dto.AlertDto;
import com.microsaas.competitorradar.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public ResponseEntity<List<AlertDto>> getAlerts() {
        return ResponseEntity.ok(alertService.getAlerts());
    }
}
