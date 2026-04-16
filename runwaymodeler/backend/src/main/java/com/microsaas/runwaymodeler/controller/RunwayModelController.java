package com.microsaas.runwaymodeler.controller;

import com.microsaas.runwaymodeler.model.FundingRound;
import com.microsaas.runwaymodeler.model.HeadcountScenario;
import com.microsaas.runwaymodeler.model.RunwayModel;
import com.microsaas.runwaymodeler.model.RunwayProjection;
import com.microsaas.runwaymodeler.service.ProjectionService;
import com.microsaas.runwaymodeler.service.RunwayModelService;
import com.microsaas.runwaymodeler.service.ScenarioService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/runway-models")
@RequiredArgsConstructor
public class RunwayModelController {

    private final RunwayModelService runwayModelService;
    private final ScenarioService scenarioService;
    private final ProjectionService projectionService;

    @PostMapping
    public ResponseEntity<RunwayModel> createModel(@RequestBody CreateModelRequest request, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(runwayModelService.createModel(request.getName(), request.getCurrentBurn(), request.getCurrentCash(), tenantId));
    }

    @PutMapping("/{id}/burn")
    public ResponseEntity<RunwayModel> updateBurn(@PathVariable UUID id, @RequestBody UpdateBurnRequest request, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(runwayModelService.updateBurn(id, request.getNewBurn(), tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RunwayModel> getModel(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return runwayModelService.getModel(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RunwayModel>> listModels(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(runwayModelService.listModels(tenantId));
    }

    @PostMapping("/{id}/scenarios/headcount")
    public ResponseEntity<HeadcountScenario> addHeadcountScenario(@PathVariable UUID id, @RequestBody AddHeadcountScenarioRequest request, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(scenarioService.addHeadcountScenario(id, request.getName(), request.getAdditionalHeads(), request.getMonthlyCostPerHead(), request.getStartDate(), tenantId));
    }

    @PostMapping("/{id}/funding-rounds")
    public ResponseEntity<FundingRound> addFundingRound(@PathVariable UUID id, @RequestBody AddFundingRoundRequest request, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(scenarioService.addFundingRound(id, request.getAmount(), request.getValuationCap(), request.getDilutionPercent(), request.getExpectedCloseDate(), tenantId));
    }

    @GetMapping("/{id}/scenarios")
    public ResponseEntity<List<HeadcountScenario>> listScenarios(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(scenarioService.listScenarios(id, tenantId));
    }

    @PostMapping("/{id}/projections")
    public ResponseEntity<List<RunwayProjection>> generate12MonthProjection(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(projectionService.generate12MonthProjection(id, tenantId));
    }

    @Data
    public static class CreateModelRequest {
        private String name;
        private BigDecimal currentBurn;
        private BigDecimal currentCash;
    }

    @Data
    public static class UpdateBurnRequest {
        private BigDecimal newBurn;
    }

    @Data
    public static class AddHeadcountScenarioRequest {
        private String name;
        private int additionalHeads;
        private BigDecimal monthlyCostPerHead;
        private LocalDate startDate;
    }

    @Data
    public static class AddFundingRoundRequest {
        private BigDecimal amount;
        private BigDecimal valuationCap;
        private Double dilutionPercent;
        private LocalDate expectedCloseDate;
    }
}
