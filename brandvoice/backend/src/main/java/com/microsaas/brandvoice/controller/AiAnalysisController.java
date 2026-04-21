package com.microsaas.brandvoice.controller;

import com.microsaas.brandvoice.entity.AnalysisReport;
import com.microsaas.brandvoice.entity.ContentAsset;
import com.microsaas.brandvoice.service.AiAnalysisService;
import com.microsaas.brandvoice.service.ContentAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
public class AiAnalysisController {
    private final AiAnalysisService aiAnalysisService;
    private final ContentAssetService contentAssetService;

    @PostMapping("/{assetId}")
    public AnalysisReport analyze(@PathVariable UUID assetId) {
        ContentAsset asset = contentAssetService.findAllByTenant(com.crosscutting.starter.tenancy.TenantContext.require())
            .stream()
            .filter(a -> a.getId().equals(assetId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Asset not found"));
        return aiAnalysisService.analyzeContent(asset);
    }
}
