package com.microsaas.compensationos.controller;

import com.microsaas.compensationos.dto.CycleScenarioRequest;
import com.microsaas.compensationos.dto.CycleScenarioResponse;
import com.microsaas.compensationos.entity.CompensationCycle;
import com.microsaas.compensationos.service.BudgetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cycles")
@RequiredArgsConstructor
@Tag(name = "Compensation Cycles")
public class CompensationCycleController {

    private final BudgetingService budgetingService;

    @GetMapping
    @Operation(summary = "Get compensation planning cycles")
    public ResponseEntity<List<CompensationCycle>> getCycles() {
        return ResponseEntity.ok(budgetingService.getCycles());
    }

    @PostMapping
    @Operation(summary = "Create a new compensation cycle")
    public ResponseEntity<CompensationCycle> createCycle(@RequestBody CompensationCycle cycle) {
        return ResponseEntity.ok(budgetingService.createCycle(cycle));
    }

    @PostMapping("/{id}/model-scenario")
    @Operation(summary = "Model a salary adjustment scenario")
    public ResponseEntity<CycleScenarioResponse> modelScenario(
            @PathVariable UUID id,
            @RequestBody CycleScenarioRequest request) {
        return ResponseEntity.ok(budgetingService.modelScenario(id, request));
    }
}
