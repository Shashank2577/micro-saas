package com.microsaas.revopsai.repository;

import com.microsaas.revopsai.model.CacCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CacCalculationRepository extends JpaRepository<CacCalculation, UUID> {
    List<CacCalculation> findByTenantId(UUID tenantId);
}
