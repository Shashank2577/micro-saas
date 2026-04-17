package com.microsaas.datalineagetracker.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.datalineagetracker.dto.LineageLinkDto;
import com.microsaas.datalineagetracker.entity.DataAsset;
import com.microsaas.datalineagetracker.entity.DataLineageLink;
import com.microsaas.datalineagetracker.repository.DataLineageLinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineageServiceTest {
    @Mock
    private DataLineageLinkRepository linkRepository;
    
    @Mock
    private DataAssetService assetService;
    
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private LineageService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateLinkAsync_fallbackToSync() {
        UUID sourceId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();
        
        LineageLinkDto dto = new LineageLinkDto();
        dto.setSourceAssetId(sourceId);
        dto.setTargetAssetId(targetId);
        dto.setTransformationLogic("SELECT * FROM source");

        DataAsset source = new DataAsset(); source.setId(sourceId);
        DataAsset target = new DataAsset(); target.setId(targetId);

        when(assetService.getAssetById(sourceId)).thenReturn(source);
        when(assetService.getAssetById(targetId)).thenReturn(target);

        // Make jdbcTemplate throw to test the fallback sync creation
        when(jdbcTemplate.update(anyString(), anyString(), anyString())).thenThrow(new RuntimeException("PGMQ not found"));

        DataLineageLink savedLink = new DataLineageLink();
        savedLink.setId(UUID.randomUUID());
        savedLink.setTenantId(tenantId);
        savedLink.setSourceAsset(source);
        savedLink.setTargetAsset(target);
        savedLink.setTransformationLogic("SELECT * FROM source");

        when(linkRepository.save(any(DataLineageLink.class))).thenReturn(savedLink);

        service.createLinkAsync(dto);

        verify(linkRepository).save(any(DataLineageLink.class));
    }
}
