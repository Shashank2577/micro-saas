package com.microsaas.taxdataorganizer.repository;

import com.microsaas.taxdataorganizer.model.TaxSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaxSummaryRepository extends JpaRepository<TaxSummary, UUID> {
    Optional<TaxSummary> findByTaxYearIdAndTenantId(UUID taxYearId, UUID tenantId);
}
