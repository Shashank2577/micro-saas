package com.microsaas.taxoptimizer.domain.repository;

import com.microsaas.taxoptimizer.domain.entity.IncomeSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncomeSourceRepository extends JpaRepository<IncomeSource, UUID> {
    List<IncomeSource> findByTenantIdAndProfileIdAndTaxYear(UUID tenantId, UUID profileId, Integer taxYear);
    Optional<IncomeSource> findByTenantIdAndId(UUID tenantId, UUID id);
}
