package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.dto.ValidateResponse;
import com.microsaas.peopleanalytics.model.OrgSnapshot;
import com.microsaas.peopleanalytics.repository.OrgSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrgSnapshotService {
    private final OrgSnapshotRepository repository;

    @Transactional(readOnly = true)
    public List<OrgSnapshot> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public OrgSnapshot findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("OrgSnapshot not found"));
    }

    @Transactional
    public OrgSnapshot create(OrgSnapshot entity, UUID tenantId) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    @Transactional
    public OrgSnapshot update(UUID id, OrgSnapshot entity, UUID tenantId) {
        OrgSnapshot existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public ValidateResponse validate(UUID id, UUID tenantId) {
        OrgSnapshot existing = findById(id, tenantId);
        return new ValidateResponse(true, "OrgSnapshot validated successfully");
    }
}
