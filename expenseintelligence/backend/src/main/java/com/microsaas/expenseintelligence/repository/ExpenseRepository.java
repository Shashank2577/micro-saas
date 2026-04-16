package com.microsaas.expenseintelligence.repository;

import com.microsaas.expenseintelligence.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findByTenantId(UUID tenantId);
    List<Expense> findByTenantIdAndExpenseDateBetween(UUID tenantId, LocalDate startDate, LocalDate endDate);
    List<Expense> findByTenantIdAndVendorAndSubmittedByAndExpenseDateBetween(UUID tenantId, String vendor, String submittedBy, LocalDate startDate, LocalDate endDate);
}
