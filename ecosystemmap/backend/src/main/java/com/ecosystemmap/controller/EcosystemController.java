package com.ecosystemmap.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.ecosystemmap.domain.DataFlow;
import com.ecosystemmap.domain.DeployedApp;
import com.ecosystemmap.domain.IntegrationOpportunity;
import com.ecosystemmap.domain.ValueMetric;
import com.ecosystemmap.dto.AppDto;
import com.ecosystemmap.dto.DataFlowDto;
import com.ecosystemmap.dto.EcosystemMapDto;
import com.ecosystemmap.service.AiInsightsService;
import com.ecosystemmap.service.EcosystemService;
import com.ecosystemmap.service.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ecosystem")

public class EcosystemController {

    private EcosystemService ecosystemService;
    private MetricsService metricsService;
    private AiInsightsService aiInsightsService;

    @GetMapping("/map")
    public ResponseEntity<EcosystemMapDto> getMap() {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(ecosystemService.getEcosystemMap(tenantId));
    }

    @GetMapping("/metrics")
    public ResponseEntity<List<ValueMetric>> getMetrics() {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(metricsService.getMetrics(tenantId));
    }

    @PostMapping("/metrics/calculate")
    public ResponseEntity<ValueMetric> calculateMetrics() {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(metricsService.calculateCompoundValue(tenantId));
    }

    @GetMapping("/opportunities")
    public ResponseEntity<List<IntegrationOpportunity>> getOpportunities() {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(aiInsightsService.getOpportunities(tenantId));
    }

    @PostMapping("/analyze")
    public ResponseEntity<List<IntegrationOpportunity>> analyzeEcosystem() {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(aiInsightsService.analyzeEcosystem(tenantId));
    }

    @PostMapping("/apps")
    public ResponseEntity<DeployedApp> registerApp(@RequestBody AppDto appDto) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(ecosystemService.registerApp(tenantId, appDto));
    }

    @PostMapping("/flows")
    public ResponseEntity<DataFlow> recordFlow(@RequestBody DataFlowDto flowDto) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(ecosystemService.recordDataFlow(tenantId, flowDto));
    }
}
