package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.TrendSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrendSignalRepository extends JpaRepository<TrendSignal, UUID> {
    List<TrendSignal> findByTenantId(UUID tenantId);
    Optional<TrendSignal> findByIdAndTenantId(UUID id, UUID tenantId);
}
