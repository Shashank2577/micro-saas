package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.ai.AIService;
import com.microsaas.datacatalogai.domain.model.Asset;
import com.microsaas.datacatalogai.domain.repository.AssetColumnRepository;
import com.microsaas.datacatalogai.domain.repository.AssetRepository;
import com.microsaas.datacatalogai.domain.repository.SemanticEmbeddingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetColumnRepository columnRepository;

    @Mock
    private SemanticEmbeddingRepository embeddingRepository;

    @Mock
    private AIService aiService;

    @InjectMocks
    private AssetService assetService;

    @Test
    void testListAssets() {
        String tenantId = "tenant1";
        Pageable pageable = PageRequest.of(0, 10);
        Asset asset = new Asset();
        asset.setId(UUID.randomUUID());
        asset.setTenantId(tenantId);
        Page<Asset> page = new PageImpl<>(List.of(asset));

        when(assetRepository.findAllByTenantId(tenantId, pageable)).thenReturn(page);

        Page<Asset> result = assetService.listAssets(tenantId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(assetRepository, times(1)).findAllByTenantId(tenantId, pageable);
    }

    @Test
    void testGetAsset() {
        String tenantId = "tenant1";
        UUID assetId = UUID.randomUUID();
        Asset asset = new Asset();
        asset.setId(assetId);
        asset.setTenantId(tenantId);

        when(assetRepository.findByIdAndTenantId(assetId, tenantId)).thenReturn(Optional.of(asset));

        Asset result = assetService.getAsset(tenantId, assetId);

        assertNotNull(result);
        assertEquals(assetId, result.getId());
        verify(assetRepository, times(1)).findByIdAndTenantId(assetId, tenantId);
    }

    @Test
    void testUpdateDescription() {
        String tenantId = "tenant1";
        UUID assetId = UUID.randomUUID();
        String description = "New Description";
        Asset asset = new Asset();
        asset.setId(assetId);
        asset.setTenantId(tenantId);

        when(assetRepository.findByIdAndTenantId(assetId, tenantId)).thenReturn(Optional.of(asset));
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArguments()[0]);

        Asset result = assetService.updateDescription(tenantId, assetId, description);

        assertNotNull(result);
        assertEquals(description, result.getDescriptionHuman());
        assertNotNull(result.getUpdatedAt());
        verify(assetRepository, times(1)).save(asset);
    }
}
