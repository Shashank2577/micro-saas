package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.CashMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CashMovementRepository extends JpaRepository<CashMovement, UUID> {
    List<CashMovement> findByTenantId(UUID tenantId);
    Optional<CashMovement> findByIdAndTenantId(UUID id, UUID tenantId);
}
