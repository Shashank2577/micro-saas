package com.microsaas.licenseguard.repository;

import com.microsaas.licenseguard.domain.SbomReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SbomReportRepository extends JpaRepository<SbomReport, UUID> {
    List<SbomReport> findByRepositoryIdAndTenantId(UUID repositoryId, UUID tenantId);
}
