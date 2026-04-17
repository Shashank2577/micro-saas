package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.domain.model.Asset;
import com.microsaas.datacatalogai.domain.repository.AssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TrustService {
    private final AssetRepository assetRepository;

    public Map<String, Object> getTrustBreakdown(String tenantId, UUID assetId) {
        Asset asset = assetRepository.findByIdAndTenantId(assetId, tenantId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("score", asset.getTrustScore());
        breakdown.put("freshness", "OK");
        breakdown.put("ownership", asset.getOwnerId() != null ? "HAS_OWNER" : "NO_OWNER");
        return breakdown;
    }

    @Transactional
    public void recomputeTrust(String tenantId, UUID assetId, Instant lastUpdatedAt) {
        Asset asset = assetRepository.findByIdAndTenantId(assetId, tenantId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        int score = 100;

        if (asset.getOwnerId() == null) {
            score -= 20;
        }

        long daysOld = ChronoUnit.DAYS.between(lastUpdatedAt, Instant.now());
        if (daysOld > 7) {
            score -= Math.min(50, daysOld * 2);
        }

        asset.setTrustScore(Math.max(0, score));
        assetRepository.save(asset);
    }
}
