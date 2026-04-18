package com.microsaas.budgetmaster.repository;

import com.microsaas.budgetmaster.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findAllByBudgetIdAndTenantId(UUID budgetId, UUID tenantId);
    List<Transaction> findAllByCategoryIdAndTenantId(UUID categoryId, UUID tenantId);
    Optional<Transaction> findByIdAndTenantId(UUID id, UUID tenantId);
}
