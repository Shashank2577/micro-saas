package com.microsaas.taxdataorganizer.repository;

import com.microsaas.taxdataorganizer.model.TaxYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaxYearRepository extends JpaRepository<TaxYear, UUID> {
    List<TaxYear> findAllByTenantId(UUID tenantId);
}
