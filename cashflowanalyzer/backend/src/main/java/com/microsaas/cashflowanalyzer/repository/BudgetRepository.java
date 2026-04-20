package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    List<Budget> findByTenantId(String tenantId);
}
