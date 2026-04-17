package com.microsaas.datastoryteller.repository;

import com.microsaas.datastoryteller.domain.model.NarrativeReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NarrativeReportRepository extends JpaRepository<NarrativeReport, UUID> {
    Page<NarrativeReport> findByTenantId(String tenantId, Pageable pageable);

    @Query("SELECT r FROM NarrativeReport r WHERE r.tenantId = :tenantId AND (:datasetId IS NULL OR r.dataset.id = :datasetId) AND (:status IS NULL OR r.status = :status)")
    Page<NarrativeReport> findByTenantIdAndFilters(String tenantId, UUID datasetId, com.microsaas.datastoryteller.domain.model.ReportStatus status, Pageable pageable);

    Optional<NarrativeReport> findByIdAndTenantId(UUID id, String tenantId);
}
