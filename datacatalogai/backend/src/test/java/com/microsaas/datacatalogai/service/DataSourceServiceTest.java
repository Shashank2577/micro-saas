package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.domain.model.Catalog;
import com.microsaas.datacatalogai.domain.model.DataSource;
import com.microsaas.datacatalogai.domain.repository.AssetColumnRepository;
import com.microsaas.datacatalogai.domain.repository.AssetRepository;
import com.microsaas.datacatalogai.domain.repository.CatalogRepository;
import com.microsaas.datacatalogai.domain.repository.DataSourceRepository;
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
public class DataSourceServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private DataSourceRepository dataSourceRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetColumnRepository columnRepository;

    @InjectMocks
    private DataSourceService dataSourceService;

    @Test
    void testListSources() {
        String tenantId = "tenant1";
        DataSource ds = new DataSource();
        ds.setId(UUID.randomUUID());
        ds.setTenantId(tenantId);

        when(dataSourceRepository.findAllByTenantId(tenantId)).thenReturn(List.of(ds));

        List<DataSource> result = dataSourceService.listSources(tenantId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(dataSourceRepository, times(1)).findAllByTenantId(tenantId);
    }

    @Test
    void testRegisterSource() {
        String tenantId = "tenant1";
        UUID catalogId = UUID.randomUUID();

        DataSource source = new DataSource();
        source.setCatalogId(catalogId);
        source.setType("POSTGRES");

        when(dataSourceRepository.save(any(DataSource.class))).thenAnswer(i -> i.getArguments()[0]);

        DataSource result = dataSourceService.registerSource(tenantId, source);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals(catalogId, result.getCatalogId());
        verify(dataSourceRepository, times(1)).save(source);
        verify(catalogRepository, never()).findAllByTenantId(anyString());
    }

    @Test
    void testRegisterSourceWithNewCatalog() {
        String tenantId = "tenant1";
        UUID newCatalogId = UUID.randomUUID();

        DataSource source = new DataSource();
        source.setType("POSTGRES");

        Catalog newCatalog = new Catalog();
        newCatalog.setId(newCatalogId);
        newCatalog.setTenantId(tenantId);

        when(catalogRepository.findAllByTenantId(tenantId)).thenReturn(List.of());
        when(catalogRepository.save(any(Catalog.class))).thenReturn(newCatalog);
        when(dataSourceRepository.save(any(DataSource.class))).thenAnswer(i -> i.getArguments()[0]);

        DataSource result = dataSourceService.registerSource(tenantId, source);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals(newCatalogId, result.getCatalogId());
        verify(catalogRepository, times(1)).findAllByTenantId(tenantId);
        verify(catalogRepository, times(1)).save(any(Catalog.class));
        verify(dataSourceRepository, times(1)).save(source);
    }
}
