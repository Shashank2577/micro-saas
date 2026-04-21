package com.microsaas.budgetpilot.repository;

import com.microsaas.budgetpilot.model.BudgetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem, UUID> {
    List<BudgetItem> findAllByBudgetIdAndTenantId(UUID budgetId, UUID tenantId);
    Optional<BudgetItem> findByIdAndTenantId(UUID id, UUID tenantId);
}
