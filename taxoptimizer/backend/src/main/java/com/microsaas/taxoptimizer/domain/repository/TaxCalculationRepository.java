package com.microsaas.taxoptimizer.domain.repository;

import com.microsaas.taxoptimizer.domain.entity.TaxCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaxCalculationRepository extends JpaRepository<TaxCalculation, UUID> {
    List<TaxCalculation> findByTenantIdAndProfileIdAndTaxYear(UUID tenantId, UUID profileId, Integer taxYear);
    Optional<TaxCalculation> findByTenantIdAndId(UUID tenantId, UUID id);
}
