package com.microsaas.vendormonitor.controller;

import com.microsaas.vendormonitor.domain.Alert;
import com.microsaas.vendormonitor.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<Alert>> getOpenAlerts() {
        return ResponseEntity.ok(alertService.getOpenAlerts());
    }

    @GetMapping("/vendors/{vendorId}/alerts")
    public ResponseEntity<List<Alert>> getAlertsForVendor(@PathVariable UUID vendorId) {
        return ResponseEntity.ok(alertService.getAlertsForVendor(vendorId));
    }

    @PutMapping("/alerts/{id}/status")
    public ResponseEntity<Alert> updateAlertStatus(@PathVariable UUID id, @RequestBody Map<String, String> payload) {
        try {
            String status = payload.get("status");
            if (status == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(alertService.updateAlertStatus(id, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
