package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.domain.model.Asset;
import com.microsaas.datacatalogai.domain.model.QueryLog;
import com.microsaas.datacatalogai.domain.model.Relationship;
import com.microsaas.datacatalogai.domain.repository.AssetRepository;
import com.microsaas.datacatalogai.domain.repository.QueryLogRepository;
import com.microsaas.datacatalogai.domain.repository.RelationshipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueryLogServiceTest {

    @Mock
    private QueryLogRepository queryLogRepository;

    @Mock
    private RelationshipRepository relationshipRepository;

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private QueryLogService queryLogService;

    @Test
    void testGetQueries() {
        String tenantId = "tenant1";
        UUID assetId = UUID.randomUUID();
        QueryLog log = new QueryLog();
        log.setId(UUID.randomUUID());
        log.setTenantId(tenantId);
        log.setAssetId(assetId);

        when(queryLogRepository.findAllByTenantIdAndAssetId(tenantId, assetId)).thenReturn(List.of(log));

        List<QueryLog> result = queryLogService.getQueries(tenantId, assetId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(queryLogRepository, times(1)).findAllByTenantIdAndAssetId(tenantId, assetId);
    }

    @Test
    void testIngestLogs() {
        String tenantId = "tenant1";
        QueryLog log1 = new QueryLog();
        QueryLog log2 = new QueryLog();
        List<QueryLog> logs = List.of(log1, log2);

        queryLogService.ingestLogs(tenantId, logs);

        assertEquals(tenantId, log1.getTenantId());
        assertEquals(tenantId, log2.getTenantId());
        verify(queryLogRepository, times(2)).save(any(QueryLog.class));
    }

    @Test
    void testInferLineageFromQuery() {
        String tenantId = "tenant1";
        UUID targetAssetId = UUID.randomUUID();
        String querySql = "INSERT INTO target_table SELECT * FROM source_table";

        Asset sourceAsset = new Asset();
        sourceAsset.setId(UUID.randomUUID());
        sourceAsset.setFqn("source_table");

        when(assetRepository.findAllByTenantId(tenantId)).thenReturn(List.of(sourceAsset));

        queryLogService.inferLineageFromQuery(tenantId, querySql, targetAssetId);

        verify(relationshipRepository, times(1)).save(any(Relationship.class));
    }
}
