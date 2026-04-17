package com.microsaas.equityintelligence.controllers;

import com.microsaas.equityintelligence.model.DilutionScenario;
import com.microsaas.equityintelligence.services.ScenarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/scenarios")
@RequiredArgsConstructor
public class ScenarioController {

    private final ScenarioService scenarioService;

    @PostMapping("/dilution")
    public ResponseEntity<DilutionScenario> simulateDilution(
            @RequestBody Map<String, Object> request,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(scenarioService.simulateDilution(request, tenantId));
    }

    @GetMapping("/waterfall")
    public ResponseEntity<Map<String, Object>> simulateWaterfall(
            @RequestParam BigDecimal exitValue,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(scenarioService.simulateWaterfall(exitValue, tenantId));
    }
}
