package com.microsaas.compensationos.controller;

import com.microsaas.compensationos.dto.EquityGrantCalculationRequest;
import com.microsaas.compensationos.dto.EquityGrantCalculationResponse;
import com.microsaas.compensationos.entity.EquityModel;
import com.microsaas.compensationos.service.EquityModelingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equity")
@RequiredArgsConstructor
@Tag(name = "Equity Modeling")
public class EquityModelingController {

    private final EquityModelingService equityModelingService;

    @GetMapping
    @Operation(summary = "Get equity models and plans")
    public ResponseEntity<List<EquityModel>> getEquityModels() {
        return ResponseEntity.ok(equityModelingService.getEquityModels());
    }

    @PostMapping("/grant-calculator")
    @Operation(summary = "Calculate projected equity grant vesting")
    public ResponseEntity<EquityGrantCalculationResponse> calculateGrant(
            @RequestBody EquityGrantCalculationRequest request) {
        return ResponseEntity.ok(equityModelingService.calculateGrant(request));
    }
}
