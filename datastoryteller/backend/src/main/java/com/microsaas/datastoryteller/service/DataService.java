package com.microsaas.datastoryteller.service;

import com.microsaas.datastoryteller.domain.model.DataSource;
import com.microsaas.datastoryteller.domain.model.Dataset;
import com.microsaas.datastoryteller.repository.DataSourceRepository;
import com.microsaas.datastoryteller.repository.DatasetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataService {

    private final DataSourceRepository dataSourceRepository;
    private final DatasetRepository datasetRepository;

    @Transactional
    public DataSource createDataSource(DataSource dataSource) {
        return dataSourceRepository.save(dataSource);
    }

    public List<DataSource> listDataSources(String tenantId) {
        return dataSourceRepository.findByTenantId(tenantId);
    }

    public DataSource getDataSource(UUID id, String tenantId) {
        return dataSourceRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Data source not found"));
    }

    @Transactional
    public void deleteDataSource(UUID id, String tenantId) {
        DataSource ds = getDataSource(id, tenantId);
        dataSourceRepository.delete(ds);
    }

    public boolean testConnection(UUID id, String tenantId) {
        DataSource ds = getDataSource(id, tenantId);
        // Simulate connection check
        return true;
    }

    @Transactional
    public Dataset registerDataset(Dataset dataset) {
        return datasetRepository.save(dataset);
    }

    public List<Dataset> listDatasets(String tenantId) {
        return datasetRepository.findByTenantId(tenantId);
    }

    public Dataset getDataset(UUID id, String tenantId) {
        return datasetRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found"));
    }

    public List<Map<String, Object>> previewDataset(UUID id, String tenantId) {
        Dataset dataset = getDataset(id, tenantId);
        // Simulate executing the query
        return List.of(
            Map.of("date", "2023-01-01", "revenue", 1000, "region", "NA", "product", "Widget"),
            Map.of("date", "2023-01-02", "revenue", 1200, "region", "EU", "product", "Gadget"),
            Map.of("date", "2023-01-03", "revenue", 1100, "region", "NA", "product", "Widget")
        );
    }
}
