package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.CashflowPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CashflowPeriodRepository extends JpaRepository<CashflowPeriod, UUID> {
    List<CashflowPeriod> findByTenantId(UUID tenantId);
    Optional<CashflowPeriod> findByIdAndTenantId(UUID id, UUID tenantId);
}
