package com.microsaas.budgetmaster.controller;

import com.microsaas.budgetmaster.domain.Alert;
import com.microsaas.budgetmaster.dto.Requests.CreateAlertRequest;
import com.microsaas.budgetmaster.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;

    @GetMapping
    public ResponseEntity<List<Alert>> getAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody CreateAlertRequest request) {
        return ResponseEntity.ok(alertService.createAlert(request));
    }
}
