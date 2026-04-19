package com.microsaas.creatoranalytics.repository;

import com.microsaas.creatoranalytics.model.ROISnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ROISnapshotRepository extends JpaRepository<ROISnapshot, UUID> {
    List<ROISnapshot> findByTenantId(UUID tenantId);
    Optional<ROISnapshot> findByIdAndTenantId(UUID id, UUID tenantId);
}
