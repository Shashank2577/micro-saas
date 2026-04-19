package com.microsaas.complianceradar.repository;

import com.microsaas.complianceradar.domain.DeadlineAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeadlineAlertRepository extends JpaRepository<DeadlineAlert, UUID> {
    List<DeadlineAlert> findAllByTenantId(UUID tenantId);
    Optional<DeadlineAlert> findByIdAndTenantId(UUID id, UUID tenantId);
    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}
