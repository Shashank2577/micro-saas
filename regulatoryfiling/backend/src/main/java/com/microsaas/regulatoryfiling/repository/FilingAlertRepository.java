package com.microsaas.regulatoryfiling.repository;

import com.microsaas.regulatoryfiling.domain.FilingAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilingAlertRepository extends JpaRepository<FilingAlert, UUID> {
    List<FilingAlert> findByTenantId(UUID tenantId);
    Optional<FilingAlert> findByIdAndTenantId(UUID id, UUID tenantId);
}
