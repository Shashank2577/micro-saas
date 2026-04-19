package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.CopyAsset;
import com.microsaas.copyoptimizer.repository.CopyAssetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CopyAssetServiceTest {

    @Mock
    private CopyAssetRepository repository;

    @InjectMocks
    private CopyAssetService service;

    @Test
    void create_ShouldSaveAndReturnEntity() {
        CopyAsset asset = new CopyAsset();
        asset.setName("Test Asset");

        when(repository.save(any(CopyAsset.class))).thenReturn(asset);

        CopyAsset result = service.create(asset);

        assertNotNull(result);
        assertEquals("Test Asset", result.getName());
        verify(repository).save(asset);
    }

    @Test
    void getById_WhenExists_ShouldReturnEntity() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        CopyAsset asset = new CopyAsset();
        asset.setId(id);
        asset.setTenantId(tenantId);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(asset));

        CopyAsset result = service.getById(id, tenantId);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getById_WhenNotExists_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById(id, tenantId));
    }
}
