package com.microsaas.creatoranalytics.service;

import com.microsaas.creatoranalytics.model.ContentAsset;
import com.microsaas.creatoranalytics.repository.ContentAssetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContentAssetServiceTest {

    @Mock
    private ContentAssetRepository repository;

    @InjectMocks
    private ContentAssetService service;

    @Test
    void testFindById_Success() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        ContentAsset asset = new ContentAsset();
        asset.setId(id);
        asset.setTenantId(tenantId);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(asset));

        ContentAsset result = service.findById(id, tenantId);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testCreate_Success() {
        UUID tenantId = UUID.randomUUID();
        ContentAsset asset = new ContentAsset();
        asset.setName("Test Asset");
        asset.setStatus("ACTIVE");

        when(repository.save(any(ContentAsset.class))).thenAnswer(invocation -> {
            ContentAsset saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        ContentAsset result = service.create(asset, tenantId);
        assertNotNull(result.getId());
        assertEquals(tenantId, result.getTenantId());
    }
}
