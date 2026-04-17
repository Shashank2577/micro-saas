package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.domain.model.AssetColumn;
import com.microsaas.datacatalogai.domain.repository.AssetColumnRepository;
import com.microsaas.datacatalogai.ai.AIService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PIIService {
    private final AssetColumnRepository columnRepository;
    private final AIService aiService;

    @Transactional
    public List<AssetColumn> detectPiiForAsset(String tenantId, UUID assetId) {
        List<AssetColumn> columns = columnRepository.findAllByTenantIdAndAssetId(tenantId, assetId);
        for (AssetColumn col : columns) {
            String sampleData = generateDummySampleData(col.getName());
            String response = aiService.detectPii(col.getName(), sampleData);

            String[] parts = response.split(",");
            if (parts.length == 2) {
                String category = parts[0].trim();
                try {
                    double confidence = Double.parseDouble(parts[1].trim());
                    if (!"NONE".equalsIgnoreCase(category) && confidence > 0.85) {
                        col.setPiiCategory(category);
                        col.setUpdatedAt(Instant.now());
                        columnRepository.save(col);
                    }
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
        return columns;
    }

    private String generateDummySampleData(String colName) {
        String lower = colName.toLowerCase();
        if (lower.contains("email")) return "test@example.com, user@domain.com";
        if (lower.contains("phone")) return "555-1234, 555-5678";
        if (lower.contains("ssn")) return "000-00-0000";
        if (lower.contains("name")) return "John Doe, Jane Smith";
        return "1, 2, 3";
    }
}
