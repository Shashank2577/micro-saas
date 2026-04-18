package com.microsaas.budgetmaster.repository;

import com.microsaas.budgetmaster.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    List<Budget> findAllByTenantId(UUID tenantId);
    Optional<Budget> findByIdAndTenantId(UUID id, UUID tenantId);
}
