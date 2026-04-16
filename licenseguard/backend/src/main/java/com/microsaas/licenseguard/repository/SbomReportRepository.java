package com.microsaas.licenseguard.repository;

import com.microsaas.licenseguard.domain.SbomReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SbomReportRepository extends JpaRepository<SbomReport, UUID> {
    List<SbomReport> findByRepositoryIdAndTenantId(UUID repositoryId, UUID tenantId);
}
