package com.microsaas.customerdiscoveryai.repository;

import com.microsaas.customerdiscoveryai.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByProjectIdAndTenantId(UUID projectId, UUID tenantId);
    Optional<Report> findByIdAndTenantId(UUID id, UUID tenantId);
}
