package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByTenantId(String tenantId);
}
