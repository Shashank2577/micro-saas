package com.microsaas.taxdataorganizer.repository;

import com.microsaas.taxdataorganizer.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findAllByTaxYearIdAndTenantId(UUID taxYearId, UUID tenantId);
}
