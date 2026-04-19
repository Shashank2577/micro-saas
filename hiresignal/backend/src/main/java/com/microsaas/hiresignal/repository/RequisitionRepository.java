package com.microsaas.hiresignal.repository;

import com.microsaas.hiresignal.model.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequisitionRepository extends JpaRepository<Requisition, UUID> {
    List<Requisition> findByTenantId(UUID tenantId);
    Optional<Requisition> findByIdAndTenantId(UUID id, UUID tenantId);
}
