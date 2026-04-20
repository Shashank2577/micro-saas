package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.SpendingPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpendingPatternRepository extends JpaRepository<SpendingPattern, UUID> {
    List<SpendingPattern> findByTenantId(String tenantId);
}
