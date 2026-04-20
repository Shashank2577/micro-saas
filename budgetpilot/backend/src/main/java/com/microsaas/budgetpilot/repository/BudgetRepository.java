package com.microsaas.budgetpilot.repository;

import com.microsaas.budgetpilot.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    List<Budget> findAllByTenantId(UUID tenantId);
    Optional<Budget> findByIdAndTenantId(UUID id, UUID tenantId);
}
