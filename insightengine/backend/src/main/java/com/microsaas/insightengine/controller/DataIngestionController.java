package com.microsaas.insightengine.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.insightengine.entity.MetricData;
import com.microsaas.insightengine.repository.MetricDataRepository;
import com.microsaas.insightengine.service.InsightDiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataIngestionController {

    private final MetricDataRepository metricDataRepository;
    private final InsightDiscoveryService insightDiscoveryService;

    @PostMapping("/data/metrics")
    public ResponseEntity<MetricData> ingestMetric(@RequestBody MetricData metricData) {
        UUID tenantId = TenantContext.require();
        metricData.setTenantId(tenantId);
        if (metricData.getTimestamp() == null) {
            metricData.setTimestamp(LocalDateTime.now());
        }
        return ResponseEntity.ok(metricDataRepository.save(metricData));
    }

    @PostMapping("/discovery/scan")
    public ResponseEntity<Void> triggerScan() {
        UUID tenantId = TenantContext.require();
        insightDiscoveryService.discoverAnomalies(tenantId, LocalDateTime.now().minusDays(1));
        return ResponseEntity.ok().build();
    }
}
