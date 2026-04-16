package com.microsaas.cashflowai.repository;

import com.microsaas.cashflowai.model.CashPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CashPositionRepository extends JpaRepository<CashPosition, UUID> {
    List<CashPosition> findByTenantIdOrderByDateAsc(UUID tenantId);
    Optional<CashPosition> findTopByTenantIdOrderByDateDesc(UUID tenantId);
}
