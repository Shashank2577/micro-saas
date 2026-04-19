package com.microsaas.cashflowai.repository;

import com.microsaas.cashflowai.model.ShortfallAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShortfallAlertRepository extends JpaRepository<ShortfallAlert, UUID> {
    List<ShortfallAlert> findByTenantId(UUID tenantId);
    Optional<ShortfallAlert> findByIdAndTenantId(UUID id, UUID tenantId);
}
