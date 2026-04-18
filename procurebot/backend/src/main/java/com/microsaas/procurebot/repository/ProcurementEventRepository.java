package com.microsaas.procurebot.repository;

import com.microsaas.procurebot.model.ProcurementEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcurementEventRepository extends JpaRepository<ProcurementEvent, UUID> {
    List<ProcurementEvent> findByTenantId(UUID tenantId);
    Optional<ProcurementEvent> findByIdAndTenantId(UUID id, UUID tenantId);
}
