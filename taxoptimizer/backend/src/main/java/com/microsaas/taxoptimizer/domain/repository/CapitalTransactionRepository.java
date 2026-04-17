package com.microsaas.taxoptimizer.domain.repository;

import com.microsaas.taxoptimizer.domain.entity.CapitalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CapitalTransactionRepository extends JpaRepository<CapitalTransaction, UUID> {
    List<CapitalTransaction> findByTenantIdAndProfileIdAndTaxYear(UUID tenantId, UUID profileId, Integer taxYear);
    Optional<CapitalTransaction> findByTenantIdAndId(UUID tenantId, UUID id);
}
