package com.microsaas.dataunification.service;

import com.microsaas.dataunification.model.DataSource;
import com.microsaas.dataunification.repository.DataSourceRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class DataSourceService {
    private final DataSourceRepository repository;

    public DataSourceService(DataSourceRepository repository) {
        this.repository = repository;
    }

    public List<DataSource> findAll() {
        return repository.findByTenantId(TenantContext.require());
    }

    public DataSource findById(UUID id) {
        return repository.findById(id).filter(ds -> ds.getTenantId().equals(TenantContext.require())).orElseThrow();
    }

    public DataSource create(DataSource ds) {
        ds.setId(UUID.randomUUID());
        ds.setTenantId(TenantContext.require());
        ds.setCreatedAt(LocalDateTime.now());
        ds.setUpdatedAt(LocalDateTime.now());
        return repository.save(ds);
    }
}
