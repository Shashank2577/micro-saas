package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.domain.model.Asset;
import com.microsaas.datacatalogai.domain.model.AssetColumn;
import com.microsaas.datacatalogai.domain.model.Catalog;
import com.microsaas.datacatalogai.domain.model.DataSource;
import com.microsaas.datacatalogai.domain.repository.AssetColumnRepository;
import com.microsaas.datacatalogai.domain.repository.AssetRepository;
import com.microsaas.datacatalogai.domain.repository.CatalogRepository;
import com.microsaas.datacatalogai.domain.repository.DataSourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataSourceService {
    private final CatalogRepository catalogRepository;
    private final DataSourceRepository dataSourceRepository;
    private final AssetRepository assetRepository;
    private final AssetColumnRepository columnRepository;

    public List<DataSource> listSources(String tenantId) {
        return dataSourceRepository.findAllByTenantId(tenantId);
    }

    @Transactional
    public DataSource registerSource(String tenantId, DataSource source) {
        if (source.getCatalogId() == null) {
            Catalog defaultCatalog = catalogRepository.findAllByTenantId(tenantId).stream().findFirst()
                .orElseGet(() -> {
                    Catalog c = new Catalog();
                    c.setTenantId(tenantId);
                    c.setName("Default Catalog");
                    return catalogRepository.save(c);
                });
            source.setCatalogId(defaultCatalog.getId());
        }
        source.setTenantId(tenantId);
        return dataSourceRepository.save(source);
    }

    @Transactional
    public void crawlSource(String tenantId, UUID sourceId) {
        DataSource source = dataSourceRepository.findByIdAndTenantId(sourceId, tenantId)
                .orElseThrow(() -> new RuntimeException("Source not found"));

        for (int i = 1; i <= 10; i++) {
            Asset asset = new Asset();
            asset.setTenantId(tenantId);
            asset.setSourceId(source.getId());
            asset.setFqn("public.table_" + i);
            asset.setType("TABLE");
            asset.setRowCountEstimate((long) (Math.random() * 1000));
            assetRepository.save(asset);

            for (int j = 1; j <= 5; j++) {
                AssetColumn col = new AssetColumn();
                col.setTenantId(tenantId);
                col.setAssetId(asset.getId());
                col.setName("col_" + j);
                col.setDataType("VARCHAR");
                columnRepository.save(col);
            }
        }

        Asset revAsset = new Asset();
        revAsset.setTenantId(tenantId);
        revAsset.setSourceId(source.getId());
        revAsset.setFqn("public.orders");
        revAsset.setType("TABLE");
        revAsset.setRowCountEstimate(5000L);
        assetRepository.save(revAsset);

        AssetColumn revCol = new AssetColumn();
        revCol.setTenantId(tenantId);
        revCol.setAssetId(revAsset.getId());
        revCol.setName("amount_usd");
        revCol.setDataType("DECIMAL");
        columnRepository.save(revCol);

        AssetColumn piiCol = new AssetColumn();
        piiCol.setTenantId(tenantId);
        piiCol.setAssetId(revAsset.getId());
        piiCol.setName("customer_email");
        piiCol.setDataType("VARCHAR");
        columnRepository.save(piiCol);
    }
}
