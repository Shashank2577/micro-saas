package com.microsaas.creatoranalytics.service;

import com.microsaas.creatoranalytics.model.ROISnapshot;
import com.microsaas.creatoranalytics.repository.ROISnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ROISnapshotService {
    private final ROISnapshotRepository repository;

    @Transactional(readOnly = true)
    public List<ROISnapshot> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public ROISnapshot findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("ROISnapshot not found"));
    }

    @Transactional
    public ROISnapshot create(ROISnapshot entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @Transactional
    public ROISnapshot update(UUID id, ROISnapshot update, UUID tenantId) {
        ROISnapshot existing = findById(id, tenantId);
        if (update.getName() != null) existing.setName(update.getName());
        if (update.getStatus() != null) existing.setStatus(update.getStatus());
        if (update.getMetadata() != null) existing.setMetadata(update.getMetadata());
        return repository.save(existing);
    }
}
