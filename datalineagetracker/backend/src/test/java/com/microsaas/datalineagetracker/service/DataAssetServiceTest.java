package com.microsaas.datalineagetracker.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.datalineagetracker.dto.AssetDto;
import com.microsaas.datalineagetracker.entity.DataAsset;
import com.microsaas.datalineagetracker.repository.DataAssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataAssetServiceTest {

    @Mock
    private DataAssetRepository repository;

    @InjectMocks
    private DataAssetService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateAsset() {
        AssetDto dto = new AssetDto();
        dto.setName("Test Asset");
        dto.setType("TABLE");

        DataAsset savedAsset = new DataAsset();
        savedAsset.setId(UUID.randomUUID());
        savedAsset.setTenantId(tenantId);
        savedAsset.setName("Test Asset");

        when(repository.save(any(DataAsset.class))).thenReturn(savedAsset);

        DataAsset result = service.createAsset(dto);

        assertNotNull(result);
        assertEquals("Test Asset", result.getName());
        assertEquals(tenantId, result.getTenantId());
        verify(repository).save(any(DataAsset.class));
    }

    @Test
    void testGetAssetById() {
        UUID assetId = UUID.randomUUID();
        DataAsset asset = new DataAsset();
        asset.setId(assetId);
        asset.setTenantId(tenantId);

        when(repository.findByIdAndTenantId(assetId, tenantId)).thenReturn(Optional.of(asset));

        DataAsset result = service.getAssetById(assetId);

        assertNotNull(result);
        assertEquals(assetId, result.getId());
    }
}
