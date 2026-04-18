package com.microsaas.dataunification.service;

import com.microsaas.dataunification.model.SyncJob;
import com.microsaas.dataunification.repository.SyncJobRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class SyncJobService {
    private final SyncJobRepository repository;

    public SyncJobService(SyncJobRepository repository) {
        this.repository = repository;
    }

    public List<SyncJob> findAll() {
        return repository.findByTenantId(TenantContext.require());
    }

    public SyncJob findById(UUID id) {
        return repository.findById(id).filter(j -> j.getTenantId().equals(TenantContext.require())).orElseThrow();
    }

    public SyncJob create(SyncJob job) {
        job.setId(UUID.randomUUID());
        job.setTenantId(TenantContext.require());
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        job.setStatus("PENDING");
        return repository.save(job);
    }
    
    public SyncJob rollback(UUID id) {
        SyncJob job = findById(id);
        job.setStatus("FAILED");
        job.setErrorLog("Rolled back manually");
        job.setUpdatedAt(LocalDateTime.now());
        return repository.save(job);
    }
}
