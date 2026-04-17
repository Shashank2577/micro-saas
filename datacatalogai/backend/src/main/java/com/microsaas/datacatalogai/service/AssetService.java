package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.domain.model.Asset;
import com.microsaas.datacatalogai.domain.model.AssetColumn;
import com.microsaas.datacatalogai.domain.model.SemanticEmbedding;
import com.microsaas.datacatalogai.domain.repository.AssetColumnRepository;
import com.microsaas.datacatalogai.domain.repository.AssetRepository;
import com.microsaas.datacatalogai.domain.repository.SemanticEmbeddingRepository;
import com.microsaas.datacatalogai.ai.AIService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final AssetColumnRepository columnRepository;
    private final SemanticEmbeddingRepository embeddingRepository;
    private final AIService aiService;

    public Page<Asset> listAssets(String tenantId, Pageable pageable) {
        return assetRepository.findAllByTenantId(tenantId, pageable);
    }

    public Asset getAsset(String tenantId, UUID id) {
        return assetRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    public List<AssetColumn> getAssetColumns(String tenantId, UUID assetId) {
        return columnRepository.findAllByTenantIdAndAssetId(tenantId, assetId);
    }

    @Transactional
    public Asset updateDescription(String tenantId, UUID id, String description) {
        Asset asset = getAsset(tenantId, id);
        asset.setDescriptionHuman(description);
        asset.setUpdatedAt(Instant.now());
        return assetRepository.save(asset);
    }

    @Transactional
    public Asset updateOwner(String tenantId, UUID id, String ownerId) {
        Asset asset = getAsset(tenantId, id);
        asset.setOwnerId(ownerId);
        asset.setUpdatedAt(Instant.now());
        return assetRepository.save(asset);
    }

    @Transactional
    public Asset updateTags(String tenantId, UUID id, List<String> tags) {
        Asset asset = getAsset(tenantId, id);
        asset.setTagsJson(tags);
        asset.setUpdatedAt(Instant.now());
        return assetRepository.save(asset);
    }

    @Transactional
    public AssetColumn updateColumnDescription(String tenantId, UUID columnId, String description) {
        AssetColumn col = columnRepository.findByIdAndTenantId(columnId, tenantId)
                .orElseThrow(() -> new RuntimeException("Column not found"));
        col.setDescription(description);
        col.setUpdatedAt(Instant.now());
        return columnRepository.save(col);
    }

    @Transactional
    public Asset documentAssetViaAI(String tenantId, UUID assetId) {
        Asset asset = getAsset(tenantId, assetId);
        List<AssetColumn> columns = getAssetColumns(tenantId, assetId);

        String schemaDetails = "Table: " + asset.getFqn() + "\nColumns:\n" +
            columns.stream().map(c -> c.getName() + " (" + c.getDataType() + ")").collect(Collectors.joining("\n"));

        String aiDesc = aiService.generateAutoDocumentation(schemaDetails);
        asset.setDescriptionAi(aiDesc);
        asset.setUpdatedAt(Instant.now());

        updateEmbedding(asset);

        return assetRepository.save(asset);
    }

    private void updateEmbedding(Asset asset) {
        String textToEmbed = asset.getFqn() + " " + (asset.getDescriptionAi() != null ? asset.getDescriptionAi() : "");
        List<Double> vector = aiService.getEmbedding(textToEmbed);

        if (!vector.isEmpty()) {
            String vectorStr = "[" + vector.stream().map(String::valueOf).collect(Collectors.joining(",")) + "]";
            SemanticEmbedding embedding = embeddingRepository.findByTenantIdAndAssetId(asset.getTenantId(), asset.getId())
                .orElse(new SemanticEmbedding());
            embedding.setTenantId(asset.getTenantId());
            embedding.setAssetId(asset.getId());
            embedding.setTextSource(textToEmbed);
            embedding.setVector(vectorStr);
            embeddingRepository.save(embedding);
        }
    }

    public List<Asset> searchAssets(String tenantId, String query) {
        List<Double> queryVector = aiService.getEmbedding(query);
        if (queryVector.isEmpty()) {
            return List.of();
        }
        String vectorStr = "[" + queryVector.stream().map(String::valueOf).collect(Collectors.joining(",")) + "]";

        List<SemanticEmbedding> nearest = embeddingRepository.findNearestNeighbors(tenantId, vectorStr, 10);
        return nearest.stream()
                .map(emb -> assetRepository.findByIdAndTenantId(emb.getAssetId(), tenantId).orElse(null))
                .filter(a -> a != null)
                .collect(Collectors.toList());
    }
}
