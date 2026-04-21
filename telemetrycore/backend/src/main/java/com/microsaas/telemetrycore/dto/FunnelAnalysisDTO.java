package com.microsaas.telemetrycore.dto;

import java.util.Map;
import java.util.UUID;

public class FunnelAnalysisDTO {
    private UUID funnelId;
    private Map<String, Object> stepAnalysis;

    public FunnelAnalysisDTO() {}
    public FunnelAnalysisDTO(UUID funnelId, Map<String, Object> stepAnalysis) {
        this.funnelId = funnelId;
        this.stepAnalysis = stepAnalysis;
    }

    public UUID getFunnelId() { return funnelId; }
    public void setFunnelId(UUID funnelId) { this.funnelId = funnelId; }
    public Map<String, Object> getStepAnalysis() { return stepAnalysis; }
    public void setStepAnalysis(Map<String, Object> stepAnalysis) { this.stepAnalysis = stepAnalysis; }
}
