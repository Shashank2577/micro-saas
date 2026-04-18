package com.microsaas.engagementpulse.controller;

import com.microsaas.engagementpulse.model.ActionPlan;
import com.microsaas.engagementpulse.model.Alert;
import com.microsaas.engagementpulse.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Tag(name = "Alerts", description = "Manager Alerts API")
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getAlerts() {
        return ResponseEntity.ok(alertService.getUnresolvedAlerts());
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Void> resolveAlert(@PathVariable UUID id) {
        alertService.resolveAlert(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/action-plans")
    public ResponseEntity<ActionPlan> addActionPlan(@PathVariable UUID id, @RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(alertService.addActionPlan(id, payload.get("description")));
    }
}
