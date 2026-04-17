package com.microsaas.datastoryteller.api;

import com.microsaas.datastoryteller.domain.model.Insight;
import com.microsaas.datastoryteller.service.NarrativeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/attribution")
@RequiredArgsConstructor
@Tag(name = "Attribution", description = "Metric Attribution Analysis")
public class AttributionController {

    private final NarrativeService narrativeService;

    @PostMapping
    @Operation(summary = "Attribute delta")
    public ResponseEntity<Insight> attributeDelta(@RequestHeader("X-Tenant-ID") String tenantId, @RequestBody AttributionRequest req) {
        return ResponseEntity.ok(narrativeService.attributeDelta(tenantId, req.getDatasetId(), req.getMetric(), req.getDeltaPercent()));
    }
}

@Data
class AttributionRequest {
    private UUID datasetId;
    private String metric;
    private double deltaPercent;
}
