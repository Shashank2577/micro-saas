package com.crosscutting.starter.export;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExportJobRepository extends JpaRepository<ExportJob, UUID> {

    Page<ExportJob> findByTenantId(UUID tenantId, Pageable pageable);
}
