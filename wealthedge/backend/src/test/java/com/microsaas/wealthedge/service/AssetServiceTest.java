package com.microsaas.wealthedge.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.wealthedge.domain.Asset;
import com.microsaas.wealthedge.domain.AssetType;
import com.microsaas.wealthedge.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateAsset() {
        Asset asset = new Asset();
        asset.setName("Beach House");
        asset.setType(AssetType.REAL_ESTATE);
        asset.setCurrentValue(new BigDecimal("1500000"));

        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> {
            Asset saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        Asset savedAsset = assetService.createAsset(asset);

        assertNotNull(savedAsset.getId());
        assertEquals(tenantId, savedAsset.getTenantId());
        assertEquals("Beach House", savedAsset.getName());
        verify(assetRepository).save(asset);
    }

    @Test
    void testGetAllAssets() {
        Asset asset = new Asset();
        asset.setTenantId(tenantId);
        when(assetRepository.findAllByTenantId(tenantId)).thenReturn(List.of(asset));

        List<Asset> assets = assetService.getAllAssets();
        assertEquals(1, assets.size());
    }

    @Test
    void testGetAsset() {
        UUID id = UUID.randomUUID();
        Asset asset = new Asset();
        when(assetRepository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(asset));

        Asset result = assetService.getAsset(id);
        assertNotNull(result);
    }
}
